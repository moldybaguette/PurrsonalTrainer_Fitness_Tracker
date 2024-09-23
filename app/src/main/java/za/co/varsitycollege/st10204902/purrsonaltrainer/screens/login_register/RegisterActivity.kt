package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.os.UserManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.AuthManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.HomeActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the activity
        var email = findViewById<EditText>(R.id.emailInput)
        var password = findViewById<EditText>(R.id.passwordInput)
        var confirmPassword = findViewById<EditText>(R.id.passwordConfirmInput)
        var registerButton = findViewById<EditText>(R.id.registerButton)

        //onclick listeners
        registerButton.setOnClickListener {
            // Check if the passwords match
            if (password.text.toString() == confirmPassword.text.toString()) {
                // Register the user
                CoroutineScope(Dispatchers.IO).launch {
                    var authManager = AuthManager()
                    val result = authManager.registerUser(email.text.toString(), password.text.toString())
                    if (result.isSuccess) {
                        // Navigate to the next screen
                        navigateTo(this@RegisterActivity, HomeActivity::class.java, null)
                    } else {
                        // Display an error message
                        Toast.makeText(this@RegisterActivity, "Error registering user", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Display an error message
                Toast.makeText(this@RegisterActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}