package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CreateRoutineExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.OnSetsUpdatedListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.WorkoutExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityStartEmptyWorkoutBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutSet
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.ChooseCategoryFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.ExerciseAddedListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineBuilder
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineConverter
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.SlideUpPopup
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class StartEmptyWorkoutActivity : AppCompatActivity(), ExerciseAddedListener, OnSetsUpdatedListener {
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

        // Add Exercise functionality
        setupAddExerciseButton()

        RoutineBuilder.addExerciseAddedListener(this)
    }

    private fun setupAddExerciseButton()
    {
        val addButton = binding.addExerciseButton
        val popup = SlideUpPopup(
            supportFragmentManager,
            binding.chooseCategoryFragmentContainer,
            binding.chooseCategoryDismissArea,
            ChooseCategoryFragment(),
            this)

        addButton.setOnClickListener { popup.showPopup() }
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

        // Bind workout details
        setupDetails()
    }

    private fun setupDetails()
    {
        if (boundWorkout != null)
        {
            binding.workoutTitle.text = boundWorkout!!.name
            // TODO: Add the color when Michael does that

            val detailsComponent = binding.detailsComponent

            // If the workout has already taken place
            if (boundWorkout!!.durationSeconds > 0)
            {

            }
            else
            {
                // Workout StartTime
                detailsComponent.workoutStartTime.text = formatWorkoutTime(LocalDateTime.now())
                // Workout BodyWeight
            }
        }
    }

    private fun formatWorkoutTime(dateTime: LocalDateTime) : String
    {
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM 'at' HH:mm", Locale.ENGLISH)
        return dateTime.format(formatter)
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
    { // Getting data from the intent's bundle
        val extras = intent.extras
        if (extras != null)
        {

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

    override fun onExerciseAdded()
    {
        if (RoutineBuilder.hasAnExercise()) {
            try
            {
                val recyclerView = binding.workoutExercises
                val userExercises = RoutineBuilder.exercises.values.toMutableList()
                val adapter = WorkoutExercisesAdapter(userExercises, this)
                adapter.addSetUpdatedListener(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
            catch (e: Exception)
            {
                Log.e("Failed to get exercises", e.toString())
            }
        }
    }

    override fun onSetsUpdated(exerciseID: String, sets: MutableList<WorkoutSet>) {
        val setsMap = mutableMapOf<String, WorkoutSet>()
        sets.forEach {
            setsMap[it.workoutSetID] = it
        }

        val oldExercise = RoutineBuilder.exercises[exerciseID]
        val newExercise = WorkoutExercise(
            exerciseID,
            oldExercise?.exerciseName!!,
            oldExercise.category,
            setsMap,
            Date(),
            oldExercise.notes,
            oldExercise.measurementType
        )
        RoutineBuilder.addWorkoutExercise(newExercise)
    }
}