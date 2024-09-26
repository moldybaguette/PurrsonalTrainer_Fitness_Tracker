package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.ColorSpinnerAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CreateRoutineExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineExerciseListAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineListAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.WorkoutExercisesAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMadeRoutineBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

class MadeRoutineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMadeRoutineBinding
    private lateinit var exercisesRecyclerView: RecyclerView
    var boundRoutine: UserRoutine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMadeRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting data provided from navigation
        getRoutineIfExists()

        // Setting up exercises for this routine
        setupExercises()

        // Bind routine details to fields
        bindRoutineDetails()
    }

    private fun bindRoutineDetails()
    {
        if (boundRoutine != null)
        {
            val title = binding.routineTitle
            val notes = binding.notes

            title.setText(boundRoutine!!.name)
            notes.setText(boundRoutine!!.description)

            // Setting title colour
            setTitleColor(boundRoutine!!.color)

            // Setting the colour spinner and the selected item
            val spinner = binding.colorPickerSpinner
            val colors = mutableListOf("blue", "red", "orange", "yellow", "green", "purple")
            val adapter = ColorSpinnerAdapter(this, colors)
            spinner.adapter = adapter

            // Setting the selected colour to the routine color
            val position = colors.indexOf(colors.find { it == boundRoutine!!.color })
            if (position >= 0)
            {
                spinner.setSelection(position)
            }

            // Setting the onclick listener so the title color changes when the user chooses a new color
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    setTitleColor(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun setTitleColor(color: String)
    {
        val title = binding.routineTitle

        when (color)
        {
            "blue" -> title.reInitialiseComponent(R.color.blue_start, R.color.blue_end)
            "red" -> title.reInitialiseComponent(R.color.red_start, R.color.red_end)
            "orange" -> title.reInitialiseComponent(R.color.orange_start, R.color.orange_end)
            "yellow" -> title.reInitialiseComponent(R.color.yellow_start, R.color.yellow_end)
            "green" -> title.reInitialiseComponent(R.color.green_start, R.color.green_end)
            "purple" -> title.reInitialiseComponent(R.color.purple_start, R.color.purple_end)
        }
    }

    private fun setupExercises()
    {
        this.exercisesRecyclerView = binding.exercisesRecyclerView

        // Add exercises if navigated to from a routine
        if (boundRoutine != null)
        {
            val adapter = CreateRoutineExercisesAdapter(boundRoutine!!.exercises.values.toList(), this)
            exercisesRecyclerView.adapter = adapter
            exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

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