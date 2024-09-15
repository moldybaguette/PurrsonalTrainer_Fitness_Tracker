package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityCatSettingsBinding

class CatSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}