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
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineConverter

class StartEmptyWorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartEmptyWorkoutBinding
    private lateinit var exercisesRecyclerView: RecyclerView
    var boundWorkout: UserWorkout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartEmptyWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind UI to Workout fields (if they exist)
        bindWorkoutDetails()
    }

    private fun bindWorkoutDetails()
    {
        // Populating boundWorkout
        if (UserManager.user != null)
        {
            // Routine data if starting a workout from a routine
            this.getRoutineIfExists()

            // Workout data if viewing an existing workout
            this.getWorkoutIfExists()
        }

        // Setup for exercises in this workout
        setupExercises()
    }

    private fun setupExercises()
    {
        this.exercisesRecyclerView = binding.workoutExercises

        // Add exercises if navigated to from a routine
        if (boundWorkout != null)
        {
            val adapter = WorkoutExercisesAdapter(boundWorkout!!.workoutExercises.values.toList(), this)
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

            // Setting bound workout
            val routine = UserManager.user!!.userRoutines[routineID]
            if (routine != null)
            {
                this.boundWorkout = RoutineConverter().convertUserRoutineToUserWorkout(routine)
                UserManager.addUserWorkout(this.boundWorkout!!)
            }
        }
    }

    private fun getWorkoutIfExists()
    {
        // Getting data from the intent's bundle
        val extras = intent.extras
        if (extras != null) {

            // Unpacking bundle
            val bundle = extras.getBundle("data")
            val workoutID = bundle?.getString("WorkoutID")

            // Setting bound workout
            val workout = UserManager.user!!.userWorkouts[workoutID]
            if (workout != null)
            {
                this.boundWorkout = workout
                UserManager.addUserWorkout(this.boundWorkout!!)
            }
        }
    }
}