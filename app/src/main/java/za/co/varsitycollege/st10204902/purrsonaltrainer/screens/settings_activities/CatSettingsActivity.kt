package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityCatSettingsBinding

/**
 * Activity for managing cat settings.
 */
class CatSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatSettingsBinding

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}