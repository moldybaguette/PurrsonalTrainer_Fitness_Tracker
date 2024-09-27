package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CreateRoutineExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.OnSetsUpdatedListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.WorkoutExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityStartEmptyWorkoutBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutSet
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.ChooseCategoryFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.ExerciseAddedListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineBuilder
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineConverter
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.SlideUpPopup
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class StartEmptyWorkoutActivity : AppCompatActivity(), ExerciseAddedListener, OnSetsUpdatedListener {
    private lateinit var binding: ActivityStartEmptyWorkoutBinding
    private lateinit var exercisesRecyclerView: RecyclerView
    private lateinit var workoutStartTime: LocalDateTime
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

        // Add done button onclick to save the workout
        if (boundWorkout != null)
            setupDoneButton()
    }

    private fun setupDoneButton()
    {
        // Subscribing this activity to the ExerciseAddedListener for the RoutineBuilder
        RoutineBuilder.addExerciseAddedListener(this)
        // Check for existing exercises
        this.onExerciseAdded()

        binding.doneButton.setOnClickListener {
            // Update workout in database
            // Converting the RoutineBuilder info to a workout
            val newWorkout = UserWorkout(
                workoutID = boundWorkout!!.workoutID,
                workoutExercises = RoutineBuilder.exercises,
                date = boundWorkout!!.date,
                name = binding.workoutTitle.text.toString(),
                durationSeconds = calculateWorkoutDuration())

            UserManager.updateUserWorkout(boundWorkout!!.workoutID, newWorkout)

            // UI stuffs (Anneme)
            binding.doneButton.setBackgroundResource(R.drawable.svg_green_bblbtn_clicked)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.doneButton.background = binding.doneButton.background
            }, 400)

            // Navigating back to home activity
            navigateTo(this, HomeActivity::class.java, null)
        }
    }
    private fun calculateWorkoutDuration() : Int
    {
        // Only recalculate the duration if the workout is being created
        if (boundWorkout!!.durationSeconds == 0)
        {
            val now = LocalDateTime.now()
            val duration = Duration.between(workoutStartTime, now)
            return duration.seconds.toInt()
        }
        return boundWorkout!!.durationSeconds
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
        RoutineBuilder.addExerciseAddedListener(this)
    }

    private fun bindWorkoutDetails()
    {
        // Populating boundWorkout
        if (UserManager.user != null)
        {
            if (!getWorkoutIfExists() && !getRoutineIfExists())
            {
                // set BoundWorkout to a new Workout and add it to the database
                this.boundWorkout = UserWorkout()
                UserManager.addUserWorkout(this.boundWorkout!!)

                // TODO: Remove the exercises from RoutineBuilder for empty workout
            }
            else // Add existing exercises in the boundWorkout to the RoutineBuilder
            {
                for (exercise in boundWorkout!!.workoutExercises)
                {
                    RoutineBuilder.addWorkoutExercise(exercise.value)
                }
            }
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
                val startDate = LocalDateTime.now()
                detailsComponent.workoutStartTime.text = formatWorkoutTime(startDate)
                this.workoutStartTime = startDate
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
    private fun getRoutineIfExists() : Boolean
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
                return true
            }
        }
        return false
    }

    private fun getWorkoutIfExists() : Boolean
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
                return true
            }
        }
        return false
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