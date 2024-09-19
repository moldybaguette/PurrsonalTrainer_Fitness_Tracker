package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityPastWorkoutDetailsBinding

class PastWorkoutDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPastWorkoutDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

    }
}