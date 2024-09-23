package za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityCreateRoutineBinding

class CreateRoutineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRoutineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_routine)

        val doneButton: AppCompatButton = findViewById(R.id.doneButton) //getting the register button
        val originalBackground =
            doneButton.background //getting the original background of the button

        // Set the on click listener for the register button
        doneButton.setOnClickListener {
            doneButton.setBackgroundResource(R.drawable.svg_green_bblbtn_clicked) // Set the background to the clicked background

            // Revert the background after 2 seconds (2000 milliseconds)
            Handler(Looper.getMainLooper()).postDelayed({
                doneButton.background = originalBackground
            }, 400)

        }
    }
}