package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.Validator
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) //navigating to the register view

        val registerButton: AppCompatButton = findViewById(R.id.registerButton) //getting the register button
        val originalBackground = registerButton.background //getting the original background of the button

        // Set the layout for the activity
        var email = findViewById<EditText>(R.id.emailInput)
        var password = findViewById<EditText>(R.id.passwordInput)
        var confirmPassword = findViewById<EditText>(R.id.passwordConfirmInput)
        Log.d("RegisterActivity", "OnCreate happened")
        // Set the on click listener for the register button
        registerButton.setOnClickListener {
            Log.d("RegisterActivity", "Register button clicked")
            registerButton.setBackgroundResource(R.drawable.svg_purple_bblbtn_clicked) // Set the background to the clicked background

            // Revert the background after 2 seconds (2000 milliseconds)
            Handler(Looper.getMainLooper()).postDelayed({
                registerButton.background = originalBackground
            }, 400)

            val validator = Validator()
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            // Validate email
            if (!validator.validateEmail(emailText)) {
                email.error = "Invalid email"
               Log.d("RegisterActivity","Invalid email")
                return@setOnClickListener
            }

            // Check if passwords match
            if (passwordText != confirmPasswordText) {
                confirmPassword.error = "Passwords do not match"
                Log.d("RegisterActivity","Passwords do not match")
                return@setOnClickListener
            }

            // Validate password complexity
            if (!validator.validatePasswordComplexity(passwordText)) {
                password.error = "Password not valid"
                Log.d("RegisterActivity","Password not valid")
                return@setOnClickListener
            }

            // Register the user
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("RegisterActivity","the coroutine is running")
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