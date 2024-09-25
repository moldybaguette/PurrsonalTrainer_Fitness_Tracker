package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RmAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentRecordsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User

class RecordsFragment : Fragment() {
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        populateExerciseTable(UserManager.user!!)
        //Create rows for each RM
        generateRMRows(UserManager.user!!)
        return binding.root
    }

    private fun populateExerciseTable(user: User) {
        val exerciseTable = binding.exerciseTable

        //get user exercises
        val userExercises = user.userExercises

        val customFont = ResourcesCompat.getFont(requireContext(), R.font.knicknack_medium)
        //Create header row for exercise names
        val headerRow = TableRow(context)
        val headerParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        headerRow.layoutParams = headerParams
        headerRow.setBackgroundColor(resources.getColor(R.color.listGrey))

        val uniqueExerciseNames = mutableSetOf<String>()

        // Add exercise names as headers
        for (exercise in userExercises) {
            if (uniqueExerciseNames.add(exercise.value.exerciseName)) {
                val textView = TextView(context).apply {
                    text = exercise.value.exerciseName
                    setPadding(10, 10, 10, 10)
                    textSize = 18f
                    setTypeface(customFont, Typeface.BOLD)
                }
                headerRow.addView(textView)
            }
        }
        exerciseTable.addView(headerRow)


    }

    private fun generateRMRows(user: User) {
        val maxRM = getMaxRepCount(user)
        val rmList = mutableListOf<String>()

        rmList.add("Rep Max")
        //Populate RM list
        for (rm in 1..maxRM) {
            rmList.add("$rm RM")
        }

        //Set up the RecyclerView with the adapter
        val rmRecyclerView = binding.rmColumn
        val rmAdapter = RmAdapter(rmList)
        rmRecyclerView.adapter = rmAdapter
        rmRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    //Get the max rep count for a user
    private fun getMaxRepCount(user: User): Int {
        var maxRepCount = 0 //default 0 reps
        for (userWorkout in user.userWorkouts) {
            for (workoutExercise in userWorkout.value.workoutExercises) {
                if (workoutExercise.value.reps != null) {
                    if (workoutExercise.value.reps!! > maxRepCount) {
                        maxRepCount = workoutExercise.value.reps!!
                    }
                }
            }
        }
        return maxRepCount
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}