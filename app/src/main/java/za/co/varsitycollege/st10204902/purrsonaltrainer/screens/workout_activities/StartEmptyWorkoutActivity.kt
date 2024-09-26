package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.WorkoutExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityStartEmptyWorkoutBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

class StartEmptyWorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartEmptyWorkoutBinding
    private lateinit var exercisesRecyclerView: RecyclerView
    var boundRoutine: UserRoutine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartEmptyWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting data provided from navigation
        this.getRoutineIfExists()

        // Setup for exercises in this workout
        setupExercises()



    }

    private fun setupExercises()
    {
        this.exercisesRecyclerView = binding.workoutExercises

        // Add exercises if navigated to from a routine
        if (boundRoutine != null)
        {
            val adapter = WorkoutExercisesAdapter(boundRoutine!!.exercises.values.toList(), this)
            exercisesRecyclerView.adapter = adapter
            exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    /**
     * Gets the routine using a routine id passed when this activity is navigated to.
     * If the user clicked on a specific routine to navigate here, the boundRoutine will be set.
     */
    private fun getRoutineIfExists()
    {
        // Getting data from the intent's bundle
        val extras = intent.extras
        if (extras != null) {

            // Unpacking bundle
            val bundle = extras.getBundle("data")
            val routineID = bundle?.getString("routineID")

            // Setting bound routine
            boundRoutine = UserManager.user!!.userRoutines[routineID] // TESTED, Works
        }
    }
}