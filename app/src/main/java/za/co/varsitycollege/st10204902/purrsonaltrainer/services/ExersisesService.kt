package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exersises
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.content.Context

class ExersisesService {

    fun loadObjectsFromJson(context: Context): List<Exersises> {
        // Open the JSON file from assets
        val jsonFile = context.assets.open("globalExercises.json")

        // Use InputStreamReader to read the file
        val reader = InputStreamReader(jsonFile)

        // Create a type token for List<MyObject>
        val listType = object : TypeToken<List<Exersises>>() {}.type

        // Parse the JSON using Gson
        val ExersisesList: List<Exersises> = Gson().fromJson(reader, listType)

        // Close the reader
        reader.close()

        return ExersisesList
    }

    //method to get unique categories from exercisesList
    fun getUniqueCategories(context: Context): List<String> {
        val exercisesList = loadObjectsFromJson(context)
        val categories = mutableListOf<String>()
        for (exercise in exercisesList) {
            if (!categories.contains(exercise.Category)) {
                categories.add(exercise.Category)
            }
        }
        return categories
    }



}