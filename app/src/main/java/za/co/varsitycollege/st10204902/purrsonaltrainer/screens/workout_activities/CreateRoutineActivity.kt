package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.ColorSpinnerAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CreateRoutineExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.OnSetsUpdatedListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityCreateRoutineBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutSet
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.ChooseCategoryFragment
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.ExerciseAddedListener
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.RoutineBuilder
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.SetBuilder
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo
import java.util.Date

class CreateRoutineActivity : AppCompatActivity(), ExerciseAddedListener, OnSetsUpdatedListener {
    private lateinit var binding: ActivityCreateRoutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doneButton: AppCompatButton = findViewById(R.id.doneButton)
        val originalBackground = doneButton.background

        val txtRoutineName = binding.untitledRoutineTitle
        val routineColor = binding.colorPickerSpinner
        val txtRoutineDescription = binding.notes


        // Subscribing this activity to the ExerciseAddedListener for the RoutineBuilder
        RoutineBuilder.addExerciseAddedListener(this)
        // Check for existing exercises
        this.onExerciseAdded()

        doneButton.setOnClickListener {

            RoutineBuilder.setRoutineName(txtRoutineName.text.toString())
            RoutineBuilder.setRoutineColor(routineColor.selectedItem.toString())
            RoutineBuilder.setRoutineDescription(txtRoutineDescription.text.toString())

            if (RoutineBuilder.hasAnExercise()) {
                UserManager.addUserRoutine(RoutineBuilder.buildRoutine())
            } else {
                // if the user has not added any exercises then show a message to the user
                Log.d("CreateRoutineActivity", "No exercises added")
            }


            doneButton.setBackgroundResource(R.drawable.svg_green_bblbtn_clicked)
            Handler(Looper.getMainLooper()).postDelayed({
                doneButton.background = originalBackground
            }, 400)

            // Navigating back to home activity
            navigateTo(this, HomeActivity::class.java, null)
        }

        val addExerciseButton: FrameLayout = findViewById(R.id.addExerciseButton)
        addExerciseButton.setOnClickListener {
            showChooseCategoryFragment()
        }

        setupColorSpinner()
    }

    private fun showChooseCategoryFragment() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val fragmentContainer: View = findViewById(R.id.chooseCategoryFragmentContainer)
        fragmentContainer.startAnimation(slideUp)
        fragmentContainer.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.chooseCategoryFragmentContainer, ChooseCategoryFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun setupColorSpinner() {
        val spinner = binding.colorPickerSpinner
        val colors = mutableListOf("blue", "red", "orange", "yellow", "green", "purple")
        val adapter = ColorSpinnerAdapter(this, colors)
        spinner.adapter = adapter
    }

    /**
     * Code run when an exercise has been added to the RoutineBuilder
     */
    override fun onExerciseAdded() {
        if (RoutineBuilder.hasAnExercise()) {
            try {
                val recyclerView = binding.routineAddedExercises
                val userExercises = RoutineBuilder.exercises.values.toMutableList()
                val adapter = CreateRoutineExercisesAdapter(userExercises, this)
                adapter.addSetUpdatedListener(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            } catch (e: Exception) {
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