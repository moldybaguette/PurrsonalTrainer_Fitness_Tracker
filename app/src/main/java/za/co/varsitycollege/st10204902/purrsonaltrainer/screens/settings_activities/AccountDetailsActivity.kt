package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityAccountDetailsBinding

class AccountDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}