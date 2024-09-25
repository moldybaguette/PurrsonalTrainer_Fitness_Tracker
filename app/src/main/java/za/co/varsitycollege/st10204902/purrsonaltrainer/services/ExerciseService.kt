package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.CreateID
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.UserManager
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import java.io.InputStreamReader

class ExerciseService(private val context: Context) {

    //                          PROPERTIES                       //
    //-----------------------------------------------------------//

    /**
     * List of all exercises including default and custom
     */
    val exercisesList = loadObjectsFromJson()

    /**
     * List of all default exercise categories
     */
    val defaultCategories = listOf(
        "lower back",
        "biceps",
        "traps",
        "adductors",
        "abs",
        "middle back",
        "glutes",
        "neck",
        "calves",
        "shoulders",
        "triceps",
        "forearms",
        "abductors",
        "hamstrings",
        "quads",
        "lats",
        "chest"
    )

    //-----------------------------------------------------------//
    //                          METHODS                          //
    //-----------------------------------------------------------//

    /**
     * Load all exercises from the JSON file and add users exercises if the user has any
     * @return List<Exercise> - List of all exercises
     */
    private fun loadObjectsFromJson(): List<Exercise> {
        // Open the JSON file from assets
        val jsonFile = context.assets.open("globalExercises.json")
        // Use InputStreamReader to read the file
        val reader = InputStreamReader(jsonFile)
        // Create a type token for List<MyObject>
        val listType = object : TypeToken<List<Exercise>>() {}.type
        // Parse the JSON using Gson
        val listOfAllExercises: List<Exercise> = Gson().fromJson(reader, listType)
        // Close the reader
        reader.close()
        UserManager.user?.userExercises?.let {
            listOfAllExercises.plus(it.values)
        }
        return listOfAllExercises
    }

    /**
     * Get all exercises in a specific category
     * @param category - Category to filter exercises by
     * @return List<Exercise> - List of exercises in the category
     */
    fun getExerciseInCategory(category: String): List<Exercise> {
        if (category.isNotEmpty()) {
            return exercisesList.filter { it.category == category }
        }
        return exercisesList
    }

    /**
     * checks the user's exercises and adds any that are not already in the exercisesList
     */
    fun updateExerciseService() {
       // add any exercises that do not already exist in exercisesList to the list from the userManager
        UserManager.user?.userExercises?.let {
            it.values.forEach { userExercise ->
                if (!exercisesList.contains(userExercise)) {
                    exercisesList.plus(userExercise)
                }
            }
        }
    }

    /**
     * Search for exercises in the list of exercises
     * @param search - Search query
     * @param listToSearch - List of exercises to search
     * @return List<Exercise> - List of exercises that match the search query
     */
    fun searchExercises(search: String, listToSearch: List<Exercise>): List<Exercise> {
        if (search.isEmpty()) {
            return listToSearch
        }
        return listToSearch.filter { it.exerciseName.contains(search, ignoreCase = true) }
    }

}
