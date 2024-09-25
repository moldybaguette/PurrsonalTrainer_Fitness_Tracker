package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityStartWorkoutBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class StartWorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val makeNewRoutineButton: LinearLayout = findViewById(R.id.makeNewRoutineButton)
        makeNewRoutineButton.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivity(intent)
        }

        // Navigation to StartEmptyWorkoutActivity
        binding.startEmptyWorkoutButton.setOnClickListener {
            navigateTo(this, StartEmptyWorkoutActivity::class.java, null)
        }
    }
}