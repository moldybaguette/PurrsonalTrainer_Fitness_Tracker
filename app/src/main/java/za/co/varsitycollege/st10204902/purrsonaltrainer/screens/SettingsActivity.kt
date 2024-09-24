package za.co.varsitycollege.st10204902.purrsonaltrainer.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivitySettingsBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities.AccountDetailsActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.accountButton.setOnClickListener(){
            navigateTo(this, AccountDetailsActivity::class.java, null)
        }



    }
}