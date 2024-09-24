package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.settings_activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager.user
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityAccountDetailsBinding

class AccountDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailsBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        binding.emailInput.setText(auth.currentUser?.email)

        //Get provider type from firebase provider
        currentUser?.let { user ->
            val providerData = user.providerData
            for (profile in providerData) {
                when (profile.providerId) {
                    "google.com" -> {
                        binding.emailInput.isEnabled=false
                        binding.passwordInput.isEnabled=false
                        val hint = "Google users cannot edit email or password"

                        Toast.makeText(this, hint, Toast.LENGTH_LONG).show()

                    }
                }
            }
        }

    }
}