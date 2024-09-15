package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityHomeBinding

class HomeLoginRegisterActivity : AppCompatActivity() {
    //THIS IS THE FIRST PAGE IN UI FLOW
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}