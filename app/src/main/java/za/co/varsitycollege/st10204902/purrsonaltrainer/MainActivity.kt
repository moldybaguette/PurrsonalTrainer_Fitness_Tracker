package za.co.varsitycollege.st10204902.purrsonaltrainer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.databaseAPI.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMainBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Item
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "testing")


        // START OF TESTING THE STATEFLOW CHANGES
        val userId = "24c4c541-95fe-49bd-8b7c-34c857f45b73" // Replace with actual user ID

        // Login user using UserManager
        UserManager.loginUser(userId)



        // Modify user's properties and collections
        modifyUserData()


    }

    private fun modifyUserData() {
        lifecycleScope.launch {
            // Wait for UserManager to load the user
            delay(2000) // Adjust delay as needed

            // Modify user's properties
            UserManager.updateUserName("the white bill gates")

            UserManager.updateExperiencePoints("69420")

            // Add a new routine
            val newRoutine = UserRoutine(
                routineID = "rarxd",
                name = "sexy workout",
                exercises = mapOf(
                    "exercise2" to Exercise(
                        exerciseID = "exercise2",
                        exerciseName = "pullups",
                        category = "Strength"
                    )
                )
            )
            UserManager.addUserRoutine(newRoutine)

            // Add an item to inventory
            val newItem = Item(
                itemID = "item2",
                name = "money",
                description = "buys stuff",
                cost = 1,
                itemURI = "https://medium.com/@zorbeytorunoglu/gson-on-android-9a6c34cb7044"
            )
            UserManager.addItemToInventory(newItem)
        }
    }
    // END OF TESTING THE STATEFLOW CHANGES
}