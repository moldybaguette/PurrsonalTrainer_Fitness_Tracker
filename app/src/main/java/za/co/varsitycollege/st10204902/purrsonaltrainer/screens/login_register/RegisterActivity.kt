package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.Validator
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        // Set the layout for the activity
        var email = findViewById<EditText>(R.id.emailInput)
        var password = findViewById<EditText>(R.id.passwordInput)
        var confirmPassword = findViewById<EditText>(R.id.passwordConfirmInput)
        var registerButton = findViewById<MaterialButton>(R.id.registerButton)

        //onclick listeners
        registerButton.setOnClickListener {

            val validator = Validator()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            // Validate email
            if (!validator.validateEmail(emailText)) {
                email.error = "Invalid email"
                return@setOnClickListener
            }

            // Check if passwords match
            if (passwordText != confirmPasswordText) {
                confirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            // Validate password complexity
            if (!validator.validatePasswordComplexity(passwordText)) {
                password.error = "Password not valid"
                return@setOnClickListener
            }

            // Register the user
            CoroutineScope(Dispatchers.IO).launch {
                val authManager = AuthManager()
                val result = authManager.registerUser(emailText, passwordText)

                if (result.isSuccess) {
                    val data = result.getOrNull()
                    if (data != null) {
                        try {
                            UserManager.userManagerScope.launch {
                                async {
                                    UserManager.setUpSingleton(data.toString())
                                }.await()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            runOnUiThread {
                                Toast.makeText(this@RegisterActivity, "Error setting up user", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Unable to complete registration. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}
