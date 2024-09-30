package za.co.varsitycollege.st10204902.purrsonaltrainer.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivitySettingsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic.SoundManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities.AccountDetailsActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities.BreakdownActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities.CatSettingsActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var soundManager: SoundManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        soundManager = SoundManager(this, R.raw.custom_tap_sound)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.accountButton.setOnClickListener(){
            soundManager.playSound()
            binding.accountButton.setBackgroundResource(R.drawable.svg_orange_bblbtn_clicked)
            navigateTo(this, AccountDetailsActivity::class.java, null)
        }

        binding.catSettingsButton.setOnClickListener(){
            soundManager.playSound()
            binding.catSettingsButton.setBackgroundResource(R.drawable.svg_green_bblbtn_clicked)
            Toast.makeText(this, "Cat Settings is not yet implemented", Toast.LENGTH_SHORT).show()
            navigateTo(this, CatSettingsActivity::class.java, null)
        }
        
        binding.statisticsButton.setOnClickListener(){
            soundManager.playSound()
            binding.statisticsButton.setBackgroundResource(R.drawable.svg_purple_bblbtn_clicked)
            navigateTo(this, BreakdownActivity::class.java, null)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateTo(this, HomeActivity::class.java, null)
        finish()
    }
}