package za.co.varsitycollege.st10204902.purrsonaltrainer

import androidx.biometric.BiometricManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMainBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register.HomeLoginRegisterActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "testing")

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        setupBiometricPrompt()

        // Check if a user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkBiometricSupport()

        } else {
            // No user is logged in, navigate to the login/register screen
            Log.d("MainActivity", "No user logged in")
            navigateTo(this, HomeLoginRegisterActivity::class.java, null)
        }
    }

    private fun checkBiometricSupport() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> authenticate()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Toast.makeText(
                this,
                "No biometric features available on this device.",
                Toast.LENGTH_SHORT
            ).show()

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Toast.makeText(
                this,
                "Biometric features are currently unavailable.",
                Toast.LENGTH_SHORT
            ).show()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> navigateTo(
                this,
                HomeLoginRegisterActivity::class.java,
                null
            )
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateTo(this@MainActivity, HomeLoginRegisterActivity::class.java, null)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateTo(this@MainActivity, HomeActivity::class.java, null)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateTo(this@MainActivity, HomeLoginRegisterActivity::class.java, null)
                }
            })


        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun authenticate() {
        biometricPrompt.authenticate(promptInfo)
    }
}