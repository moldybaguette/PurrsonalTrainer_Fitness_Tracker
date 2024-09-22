package za.co.varsitycollege.st10204902.purrsonaltrainer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import za.co.valsitycollege.st10204902.purrsonaltrainer.models.*
import za.co.varsitycollege.st10204902.purrsonaltrainer.databaseAPI.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.databaseAPI.UserManager.loginUser
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "testing")

// create a sample user and add them to the database
        //val sampleUser = createSampleUser()
        //FirebaseDatabase.getInstance().reference.child("users").child(sampleUser.userID).setValue(sampleUser)

        // START OF TESTING THE STATEFLOW CHANGES
        val userID = "24c4c541-95fe-49bd-8b7c-34c857f45b73"

               val jobby =  UserManager.userManagerScope.launch {
                   println("about to login")
                   async {   loginUser(userID)}.await()
                    println("after login")
                }

        println("when does this print")
        jobby.invokeOnCompletion { println("this was invoked on completion")
        testUserManagerMethods()}




        }

    }



        fun testUserManagerMethods() {
            // Sample data
            val sampleUser = User(
                userID = "user123",
                name = "ronny coleman",
                catName = "Whiskers",
                milkCoins = "100",
                experiencePoints = "2000",
                backgroundURI = "https://example.com/background.jpg",
                catURI = "https://example.com/cat.jpg"
            )

            val sampleRoutine = UserRoutine(
                routineID = "testRoutine",
                name = "Daily Routine",
                description = "A routine for daily exercises"
            )

            val sampleWorkout = UserWorkout(
                workoutID = "testWorkout",
                name = "Morning Workout"
            )

            val sampleExercise = Exercise(
                exerciseID = "testExercise",
                exerciseName = "Push-ups",
                category = "Strength"
            )

            val sampleWorkoutExercise = WorkoutExercise(
                exerciseID = "testExerciseAgain",
                exerciseName = "Push-ups",
                category = "Strength",
                reps = 20
            )

            val sampleAchievement = UserAchievement(
                achievementID = "testAchievement",
                name = "First Workout",
                description = "Completed the first workout",
                dateAchieved = "2023-10-01"
            )

            val sampleBackground = UserBackground(
                backgroundID = "testBackground",
                name = "Beach",
                backgroundURI = "https://example.com/beach.jpg"
            )

            val sampleItem = Item(
                itemID = "testItem",
                name = "Protein Bar",
                description = "A bar of protein",
                cost = 5,
                itemURI = "https://example.com/protein_bar.jpg"
            )




            // Test update methods
            UserManager.updateUserName("phil heath")
            UserManager.updateCatName("Fluffy")
            UserManager.updateExperiencePoints("3000")
            UserManager.updateBackgroundURI("pornhub.com")
            UserManager.updateCatURI("deviantart.com/furrys")
            UserManager.updateMilkCoins("6459564645")

            // Test add methods
            UserManager.addUserRoutine(sampleRoutine)
            UserManager.addUserWorkout(sampleWorkout)
            UserManager.addUserExercise(sampleExercise)
            UserManager.addExerciseToWorkout("workout1", sampleWorkoutExercise)
            UserManager.addUserAchievement(sampleAchievement)
            UserManager.addUserBackground(sampleBackground)
            UserManager.addItemToInventory(sampleItem)

            // Print the updated user
            //println(UserManager.user)
        }
    fun createSampleUser(): User {
        val sampleExercises = mapOf(
            "exercise1" to Exercise(
                exerciseID = "exercise1",
                exerciseName = "Push-ups",
                category = "Strength"
            ),
            "exercise2" to Exercise(
                exerciseID = "exercise2",
                exerciseName = "Pull-ups",
                category = "Strength"
            )
        )

        val sampleWorkouts = mapOf(
            "workout1" to UserWorkout(
                workoutID = "workout1",
                name = "Morning Workout",
                workoutExercises = mapOf(
                    "exercise1" to WorkoutExercise(
                        exerciseID = "exercise1",
                        exerciseName = "Push-ups",
                        category = "Strength",
                        reps = 20
                    )
                ),
                durationSeconds = 1800
            )
        )

        val sampleRoutines = mapOf(
            "routine1" to UserRoutine(
                routineID = "routine1",
                name = "Daily Routine",
                description = "A routine for daily exercises",
                exercises = sampleExercises
            )
        )

        val sampleAchievements = mapOf(
            "achievement1" to UserAchievement(
                achievementID = "achievement1",
                name = "First Workout",
                description = "Completed the first workout",
                dateAchieved = "2023-10-01"
            )
        )

        val sampleBackgrounds = mapOf(
            "background1" to UserBackground(
                backgroundID = "background1",
                name = "Beach",
                backgroundURI = "https://example.com/beach.jpg"
            )
        )

        val sampleInventory = mapOf(
            "item1" to Item(
                itemID = "item1",
                name = "Protein Bar",
                description = "A bar of protein",
                cost = 5,
                itemURI = "https://example.com/protein_bar.jpg"
            )
        )

        return User(
            userID = "user123",
            name = "ronny coleman",
            catName = "Whiskers",
            milkCoins = "100",
            experiencePoints = "2000",
            backgroundURI = "https://example.com/background.jpg",
            catURI = "https://example.com/cat.jpg",
            userRoutines = sampleRoutines,
            userWorkouts = sampleWorkouts,
            userExercises = sampleExercises,
            userAchievements = sampleAchievements,
            userBackgrounds = sampleBackgrounds,
            userInventory = sampleInventory
        )
    }

    // END OF TESTING THE STATEFLOW CHANGES
