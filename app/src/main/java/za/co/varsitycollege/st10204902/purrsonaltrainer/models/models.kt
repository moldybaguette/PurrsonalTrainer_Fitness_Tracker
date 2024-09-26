package za.co.varsitycollege.st10204902.purrsonaltrainer.models

import com.google.firebase.database.IgnoreExtraProperties
import za.co.varsitycollege.st10204902.purrsonaltrainer.backend.CreateID
import java.util.Date

// Item class for representing items in the shop
@IgnoreExtraProperties
data class Item(
    val itemID: String = CreateID.GenerateID(),
    val name: String = "",
    val description: String = "",
    val cost: Int = 0,
    val itemURI: String = ""
)

// User class representing a user and their related data
@IgnoreExtraProperties
data class User(
    val userID: String = "",
    val name: String = "",
    val catName: String = "",
    val milkCoins: String = "",
    val experiencePoints: String = "",
    val backgroundURI: String = "",
    val catURI: String = "",
    val userRoutines: Map<String, UserRoutine> = emptyMap(),
    val userWorkouts: Map<String, UserWorkout> = emptyMap(),
    val userExercises: Map<String, Exercise> = emptyMap(),
    val userAchievements: Map<String, UserAchievement> = emptyMap(),
    val userBackgrounds: Map<String, UserBackground> = emptyMap(),
    val userInventory: Map<String, Item> = emptyMap(),
    val customCategories: List<String> = emptyList()
)

// WorkoutExercise class representing an exercise during a workout
@IgnoreExtraProperties
data class WorkoutExercise(
    val exerciseID: String = CreateID.GenerateID(),
    val exerciseName: String = "",
    val category: String = "",
    val sets: Map<String,WorkoutSet> = emptyMap(),
    val date:Date = Date(),
    val notes: String = "",
    val measurementType: String = "",
)

@IgnoreExtraProperties
data class WorkoutSet(
    val workoutSetID: String = CreateID.GenerateID(),
    val weight: Int? = 0,
    val reps: Int? = 0,
    val distance: Int? = 0,
    val durationSeconds: Int? = 0,
    val setType: String = "",
)

// Exercise class for storing default and custom exercises
@IgnoreExtraProperties
data class Exercise(
    val exerciseID: String = CreateID.GenerateID(),
    val exerciseName: String = "",
    val category: String = "",
    val notes: String = "",
    val measurementType: String = "",
    val isCustom: Boolean = false
)

// UserBackground class for representing user backgrounds
@IgnoreExtraProperties
data class UserBackground(
    val backgroundID: String = CreateID.GenerateID(),
    val name: String = "",
    val backgroundURI: String = ""
)

// UserRoutine class representing a user's routine
@IgnoreExtraProperties
data class UserRoutine(
    val routineID: String = CreateID.GenerateID(),
    val name: String = "",
    val color : String = "",
    val description: String = "",
    val exercises: Map<String, WorkoutExercise> = emptyMap()
)

// UserWorkout class representing a workout within a routine
@IgnoreExtraProperties
data class UserWorkout(
    val workoutID: String = CreateID.GenerateID(),
    val name: String = "",
    val workoutExercises: Map<String, WorkoutExercise> = emptyMap(),
    val durationSeconds: Int = 0,
    val date: Date = Date()
)

// UserAchievement class representing a user's achievement
@IgnoreExtraProperties
data class UserAchievement(
    val achievementID: String = CreateID.GenerateID(),
    val name: String = "",
    val description: String = "",
    val dateAchieved: String = ""
)
