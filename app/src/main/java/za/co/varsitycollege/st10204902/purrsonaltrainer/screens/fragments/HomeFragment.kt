package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.MonthsAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.MonthWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities.StartEmptyWorkoutActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities.StartWorkoutActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var routinesRecyclerView: RecyclerView
    private lateinit var monthsAdapter: MonthsAdapter
    private var monthWorkoutList: List<MonthWorkout> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routinesRecyclerView = view.findViewById(R.id.routinesRecyclerView)
        routinesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize MonthsAdapter with empty list and click listener
        monthsAdapter = MonthsAdapter(monthWorkoutList) { workout ->
            // Handle workout item click
            val bundle = Bundle()
            bundle.putString("WorkoutID", workout.workoutID) // Correct key
            navigateTo(requireContext(), StartEmptyWorkoutActivity::class.java, bundle)
        }
        routinesRecyclerView.adapter = monthsAdapter

        val addButton: AppCompatImageButton = view.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            val intent = Intent(activity, StartWorkoutActivity::class.java)
            startActivity(intent)
        }

        // Load and group workouts by month
        loadMonthWorkouts()
    }

    private fun loadMonthWorkouts() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserManager.user
            if (user == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            // Group workouts by month
            val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            val grouped = user.userWorkouts.values.groupBy { dateFormat.format(it.date) }
                .map { (month, workouts) ->
                    MonthWorkout(month, workouts.sortedBy { it.date })
                }.sortedByDescending {
                    // To sort months in descending order, parse the month string back to Date
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(it.month) ?: Date()
                }

            monthWorkoutList = grouped

            withContext(Dispatchers.Main) {
                monthsAdapter = MonthsAdapter(monthWorkoutList) { workout ->
                    val bundle = Bundle()
                    bundle.putString("WorkoutID", workout.workoutID)
                    navigateTo(requireContext(), StartEmptyWorkoutActivity::class.java, bundle)
                }
                routinesRecyclerView.adapter = monthsAdapter
                monthsAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}