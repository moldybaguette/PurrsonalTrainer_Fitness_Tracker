package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RmAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import kotlinx.coroutines.flow.collectLatest
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.WorkoutWorker
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentRecordsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.*

class RecordsFragment : Fragment() {
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    // WorkoutWorker instance
    private lateinit var workoutWorker: WorkoutWorker

    // Data structure to hold weights per exercise and rep count
    private lateinit var exerciseWeightsMap: Map<String, Map<Int, Int>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)

        val user = UserManager.user ?: return binding.root

        // Initialize WorkoutWorker with user workouts
        workoutWorker = WorkoutWorker(user.userWorkouts)

        // Process user data to map exercises to weights and reps
        processUserData(user)

        // Determine the maximum reps achieved across all exercises
        val maxRM = getGlobalMaxRepCount(user)

        // Generate RM labels in RecyclerView
        generateRMRows(maxRM)

        // Populate the TableLayout with weights data
        populateExerciseTable(user, maxRM)

        // Synchronize scrolling
        synchronizeScrolling()

        return binding.root
    }

    /**
     * Processes user workout data to map each exercise to its reps and corresponding weights.
     */
    private fun processUserData(user: User) {
        // Temporary mutable map
        val tempMap = mutableMapOf<String, MutableMap<Int, Int>>()

        // Iterate through each workout using WorkoutWorker
        for ((workoutID, workout) in workoutWorker.usersWorkouts) {
            for ((exerciseID, workoutExercise) in workout.workoutExercises) {
                val exerciseName = workoutExercise.exerciseName
                val reps = workoutExercise.sets.values.mapNotNull { it.reps }.maxOrNull() ?: 0
                val maxWeight = workoutWorker.getTotalWeightPerExercise(exerciseID)

                if (reps > 0 && maxWeight > 0) {
                    // Initialize map for the exercise if not present
                    if (!tempMap.containsKey(exerciseName)) {
                        tempMap[exerciseName] = mutableMapOf()
                    }

                    // Populate the map using WorkoutWorker's calculateRMsPerExercise
                    val rMMap = workoutWorker.calculateRMsPerExercise(exerciseID)
                    for ((rep, weight) in rMMap) {
                        val existingWeight = tempMap[exerciseName]?.get(rep) ?: 0
                        if (weight > existingWeight) {
                            tempMap[exerciseName]?.put(rep, weight)
                        }
                    }
                }
            }
        }

        // Assign the processed map to the class variable
        exerciseWeightsMap = tempMap

        // Log the exerciseWeightsMap for debugging
        Log.d("RecordsFragment", "Exercise Weights Map: $exerciseWeightsMap")
    }

    /**
     * Determines the highest number of reps achieved across all exercises using WorkoutWorker.
     */
    private fun getGlobalMaxRepCount(user: User): Int {
        var maxRep = 0
        for ((_, workout) in workoutWorker.usersWorkouts) {
            for ((_, workoutExercise) in workout.workoutExercises) {
                val reps = workoutWorker.getMaxRepsPerExercise(workoutExercise.exerciseID)
                if (reps > maxRep) {
                    maxRep = reps
                }
            }
        }
        return maxRep
    }

    /**
     * Generates RM labels and sets up the RecyclerView.
     */
    private fun generateRMRows(maxRM: Int) {
        val rmList = mutableListOf<String>()
        rmList.add("Rep Max") // Header for RM column

        // Populate RM labels from 1 RM to maxRM RM
        for (rm in 1..maxRM) {
            rmList.add("$rm RM")
        }

        // Set up the RecyclerView with the adapter
        val rmRecyclerView = binding.rmColumn
        val rmAdapter = RmAdapter(rmList)
        rmRecyclerView.adapter = rmAdapter
        rmRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    /**
     * Populates the TableLayout with the weights lifted per exercise aligned with RM labels.
     */
    private fun populateExerciseTable(user: User, maxRM: Int) {
        val exerciseTable = binding.exerciseTable

        // Get custom font
        val customFont = ResourcesCompat.getFont(requireContext(), R.font.knicknack_medium)

        // Create header row with exercise names
        val headerRow = TableRow(context).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                48 // Fixed height to match RecyclerView items
            )
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.listGrey))
        }

        // Extract unique exercise names from processed data to avoid duplication
        val exerciseNames = exerciseWeightsMap.keys

        // Log exercise names for debugging
        Log.d("RecordsFragment", "Unique Exercise Names: $exerciseNames")

        // Add exercise names to header row
        for (exerciseName in exerciseNames) {
            val textView = TextView(context).apply {
                text = exerciseName
                setPadding(16, 16, 16, 16)
                textSize = 18f
                setTypeface(customFont, Typeface.BOLD)
            }
            headerRow.addView(textView)
        }
        exerciseTable.addView(headerRow)

        // Create rows for each RM label
        val rmLabels = mutableListOf<String>()
        rmLabels.add("Rep Max")
        for (rm in 1..maxRM) {
            rmLabels.add("$rm RM")
        }

        for (rmLabel in rmLabels) {
            val tableRow = TableRow(context).apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    48 // Fixed height to match RecyclerView items
                )
            }

            for (exerciseName in exerciseNames) {
                val weightAchieved = when (rmLabel) {
                    "Rep Max" -> {
                        // For "Rep Max", display the maximum weight achieved for the exercise
                        workoutWorker.getTotalWeightPerExercise(
                            workoutWorker.usersWorkouts.keys.firstOrNull { workoutID ->
                                workoutWorker.usersWorkouts[workoutID]?.workoutExercises?.values?.any {
                                    it.exerciseName == exerciseName
                                } ?: false
                            } ?: ""
                        ).let { if (it > 0) "$it kg" else "N/A" }
                    }
                    else -> {
                        // For "1 RM" to "maxRM RM", display the weight for that rep count
                        val rmNumber = rmLabel.replace(" RM", "").toIntOrNull() ?: 0
                        exerciseWeightsMap[exerciseName]?.get(rmNumber)?.let {
                            "$it kg"
                        } ?: "N/A"
                    }
                }

                val textView = TextView(context).apply {
                    text = weightAchieved
                    setPadding(16, 16, 16, 16)
                    textSize = 16f
                }
                tableRow.addView(textView)
            }

            exerciseTable.addView(tableRow)
        }

        // Log the populated table for debugging
        Log.d("RecordsFragment", "Exercise Table populated with weights.")
    }

    /**
     * Synchronizes the scrolling between RecyclerView and TableLayout's ScrollView.
     */
    private fun synchronizeScrolling() {
        binding.rmColumn.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.horizontalScrollView.scrollBy(0, dy)
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
