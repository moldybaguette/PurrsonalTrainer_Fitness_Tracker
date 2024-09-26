// RecordsFragment.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.TableAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.WorkoutWorker
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.FragmentRecordsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CellType
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.TableCell
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.User

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

        // Get unique exercise names
        val exerciseNames = exerciseWeightsMap.keys.toList()

        // Prepare table data
        val tableData = prepareTableData(maxRM, exerciseNames)

        // Set up RecyclerView with GridLayoutManager
        val spanCount = exerciseNames.size + 1 // +1 for RM labels
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        binding.tableRecyclerView.layoutManager = gridLayoutManager

        // Set fixed size for performance
        binding.tableRecyclerView.setHasFixedSize(true)

        // Set up the adapter with spanCount
        val tableAdapter = TableAdapter(tableData, spanCount)
        binding.tableRecyclerView.adapter = tableAdapter

        // Optional: Improve performance by disabling nested scrolling
        binding.tableRecyclerView.isNestedScrollingEnabled = false

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
     * Prepares the table data for the adapter.
     */
    private fun prepareTableData(maxRM: Int, exerciseNames: List<String>): List<TableCell> {
        val tableData = mutableListOf<TableCell>()

        // Header Row
        tableData.add(TableCell(CellType.HEADER, "Rep Max")) // Top-left corner
        for (exercise in exerciseNames) {
            tableData.add(TableCell(CellType.HEADER, exercise))
        }

        // RM Rows
        for (rm in 1..maxRM) {
            tableData.add(TableCell(CellType.LABEL, "$rm RM"))
            for (exercise in exerciseNames) {
                val weight = exerciseWeightsMap[exercise]?.get(rm)
                val displayText = if (weight != null) "$weight kg" else "---"
                tableData.add(TableCell(CellType.DATA, displayText))
            }
        }

        return tableData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
