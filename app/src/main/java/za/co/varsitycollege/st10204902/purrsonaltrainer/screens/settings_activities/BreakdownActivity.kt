package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityBreakdownBinding

class BreakdownActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreakdownBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}