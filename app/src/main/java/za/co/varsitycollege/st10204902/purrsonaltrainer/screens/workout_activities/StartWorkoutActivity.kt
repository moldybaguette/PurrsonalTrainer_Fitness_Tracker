package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityStartWorkoutBinding

class StartWorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_workout)

        val makeNewRoutineButton: AppCompatButton = findViewById(R.id.makeNewRoutineButton)
        makeNewRoutineButton.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivity(intent)
        }
    }
}