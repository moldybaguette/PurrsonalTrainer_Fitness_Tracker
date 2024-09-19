package za.co.varsitycollege.st10204902.purrsonaltrainer.models

import com.example.example.Exercise
import com.example.example.Item
import com.example.example.UserAchievement
import com.example.example.UserBackground
import com.example.example.UserRoutine
import com.example.example.UserWorkout
import com.example.example.WorkoutExercise
import com.google.gson.annotations.SerializedName

// Item class for representing items in the shop
data class Item(
    @SerializedName("item_id") var itemID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("Description") var description: String = "",
    @SerializedName("Cost") var cost: Int = 0,
    @SerializedName("ItemURI") var itemURI: String = ""
)

// User class representing a user and their related data
data class User(
    @SerializedName("user_id") var userID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("CatName") var catName: String = "",
    @SerializedName("MilkCoins") var milkCoins: String = "",
    @SerializedName("ExperiencePoints") var experiencePoints: String = "",
    @SerializedName("BackgroundURI") var backgroundURI: String = "",
    @SerializedName("CatURI") var catURI: String = "",
    @SerializedName("UserRoutines") val userRoutines: MutableMap<String, UserRoutine> = mutableMapOf(),
    @SerializedName("UserWorkouts") val userWorkouts: MutableMap<String, UserWorkout> = mutableMapOf(),
    @SerializedName("UserExercises") val userExercises: MutableMap<String, Exercise> = mutableMapOf(),
    @SerializedName("UserAchievements") val userAchievements: MutableMap<String, UserAchievement> = mutableMapOf(),
    @SerializedName("UserInventory") val userInventory: MutableMap<String, Item> = mutableMapOf(),
    @SerializedName("UserBackgrounds") val userBackgrounds: MutableMap<String, UserBackground> = mutableMapOf()
)

// UserAchievement class representing unlocked achievements
data class UserAchievement(
    @SerializedName("AchievementID") var achievementID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("CatImageURI") var CatImageURI: String = "",
    @SerializedName("CatFact") var catFact: String = ""
)

// UserRoutine class representing workout routines
data class UserRoutine(
    @SerializedName("routine_id") var routineID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("Exercises") val exercises: MutableMap<String, Exercise> = mutableMapOf()
)

// UserWorkout class representing individual workouts
data class UserWorkout(
    @SerializedName("userWorkout_id") var userWorkoutID: String = "",
    @SerializedName("StartDateTime") var start: String = "",
    @SerializedName("EndDateTime") var end: String = "",
    @SerializedName("RoutineID") var routineID: String = "",
    @SerializedName("WorkoutExercises") val workoutExercises: MutableMap<String, WorkoutExercise> = mutableMapOf()
)

// WorkoutExercise class representing individual exercises performed during a workout
data class WorkoutExercise(
    @SerializedName("ExerciseID") var exerciseID: String = "",
    @SerializedName("ExerciseName") var exerciseName: String = "",
    @SerializedName("Category") var category: String = "",
    @SerializedName("Weight(kg)") var weight: Int? = 0,
    @SerializedName("Reps") var reps: Int? = 0,
    @SerializedName("Distance") var distance: Int? = 0,
    @SerializedName("DurationSeconds") var durationSeconds: Int? = 0,
    @SerializedName("Notes") var notes: String = ""
)

// Exercise class for storing default and custom exercises
data class Exercise(
    @SerializedName("exercise_id") var exerciseID: String = "",
    @SerializedName("ExerciseName") var exerciseName: String = "",
    @SerializedName("Category") var category: String = ""
)

// UserBackground class for representing user backgrounds
data class UserBackground(
    @SerializedName("BackgroundID") var backgroundID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("BackgroundURI") var backgroundURI: String = ""
)
