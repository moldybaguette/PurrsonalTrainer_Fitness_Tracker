package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.ColorSpinnerAdapter
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityCreateRoutineBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.ChooseCategoryFragment

class CreateRoutineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRoutineBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =  ActivityCreateRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doneButton: AppCompatButton = findViewById(R.id.doneButton)
        val originalBackground = doneButton.background

        doneButton.setOnClickListener {
            doneButton.setBackgroundResource(R.drawable.svg_green_bblbtn_clicked)
            Handler(Looper.getMainLooper()).postDelayed({
                doneButton.background = originalBackground
            }, 400)
        }

        val addExerciseButton: ImageView = findViewById(R.id.addExerciseButton)
        addExerciseButton.setOnClickListener {
            showChooseCategoryFragment()
        }

        // Color spinner adapter
        val spinner = binding.colorPickerSpinner

        val colors = mutableListOf("blue", "red", "orange", "yellow", "green", "purple")
        val adapter = ColorSpinnerAdapter(this, colors)
        spinner.adapter = adapter
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
}