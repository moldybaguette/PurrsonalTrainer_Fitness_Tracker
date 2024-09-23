package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.login_register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.os.UserManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
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
        setContentView(R.layout.activity_register) //navigating to the register view

        val registerButton: AppCompatButton = findViewById(R.id.registerButton) //getting the register button
        val originalBackground = registerButton.background //getting the original background of the button

        // Set the on click listener for the register button
        registerButton.setOnClickListener {
            registerButton.setBackgroundResource(R.drawable.svg_purple_bblbtn_clicked) // Set the background to the clicked background

            // Revert the background after 2 seconds (2000 milliseconds)
            Handler(Looper.getMainLooper()).postDelayed({
                registerButton.background = originalBackground
            }, 400)
        }
    }
}