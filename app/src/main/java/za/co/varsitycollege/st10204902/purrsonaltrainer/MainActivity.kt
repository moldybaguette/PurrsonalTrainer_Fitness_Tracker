package za.co.varsitycollege.st10204902.purrsonaltrainer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMainBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register.HomeLoginRegisterActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "testing")

        // Temporary navigation to Home Activity
        navigateTo(this, HomeLoginRegisterActivity::class.java, null)
    }
}