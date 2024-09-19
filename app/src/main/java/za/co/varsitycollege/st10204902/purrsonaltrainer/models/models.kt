package za.co.varsitycollege.st10204902.purrsonaltrainer.models

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

// Item class for representing items in the shop
@IgnoreExtraProperties
data class Item(
    @get:PropertyName("item_id")
    @set:PropertyName("item_id")
    var itemID: String = "",

    @get:PropertyName("Name")
    @set:PropertyName("Name")
    var name: String = "",

    @get:PropertyName("Description")
    @set:PropertyName("Description")
    var description: String = "",

    @get:PropertyName("Cost")
    @set:PropertyName("Cost")
    var cost: Int = 0,

    @get:PropertyName("ItemURI")
    @set:PropertyName("ItemURI")
    var itemURI: String = ""
) {
    constructor() : this("", "", "", 0, "")
}

// User class representing a user and their related data
@IgnoreExtraProperties
data class User(
    @get:PropertyName("userID")
    @set:PropertyName("userID")
    var userID: String = "",

    @get:PropertyName("Name")
    @set:PropertyName("Name")
    var name: String = "",

    @get:PropertyName("CatName")
    @set:PropertyName("CatName")
    var catName: String = "",

    @get:PropertyName("MilkCoins")
    @set:PropertyName("MilkCoins")
    var milkCoins: String = "",

    @get:PropertyName("ExperiencePoints")
    @set:PropertyName("ExperiencePoints")
    var experiencePoints: String = "",

    @get:PropertyName("BackgroundURI")
    @set:PropertyName("BackgroundURI")
    var backgroundURI: String = "",

    @get:PropertyName("CatURI")
    @set:PropertyName("CatURI")
    var catURI: String = "",

    @get:PropertyName("UserRoutines")
    @set:PropertyName("UserRoutines")
    var userRoutines: Map<String, UserRoutine> = emptyMap(),

    @get:PropertyName("UserWorkouts")
    @set:PropertyName("UserWorkouts")
    var userWorkouts: Map<String, UserWorkout> = emptyMap(),

    @get:PropertyName("UserExercises")
    @set:PropertyName("UserExercises")
    var userExercises: Map<String, Exercise> = emptyMap(),

    @get:PropertyName("UserAchievements")
    @set:PropertyName("UserAchievements")
    var userAchievements: Map<String, UserAchievement> = emptyMap(),

    @get:PropertyName("UserInventory")
    @set:PropertyName("UserInventory")
    var userInventory: Map<String, Item> = emptyMap(),

    @get:PropertyName("UserBackgrounds")
    @set:PropertyName("UserBackgrounds")
    var userBackgrounds: Map<String, UserBackground> = emptyMap()
) {
    // Empty constructor for Firebase deserialization
    constructor() : this(
        userID = "",
        name = "",
        catName = "",
        milkCoins = "",
        experiencePoints = "",
        backgroundURI = "",
        catURI = "",
        userRoutines = emptyMap(),
        userWorkouts = emptyMap(),
        userExercises = emptyMap(),
        userAchievements = emptyMap(),
        userInventory = emptyMap(),
        userBackgrounds = emptyMap()
    )
}

// UserAchievement class representing unlocked achievements
@IgnoreExtraProperties
data class UserAchievement(
    @get:PropertyName("AchievementID")
    @set:PropertyName("AchievementID")
    var achievementID: String = "",

    @get:PropertyName("Name")
    @set:PropertyName("Name")
    var name: String = "",

    @get:PropertyName("CatImageURI")
    @set:PropertyName("CatImageURI")
    var catImageURI: String = "",

    @get:PropertyName("CatFact")
    @set:PropertyName("CatFact")
    var catFact: String = ""
) {
    constructor() : this("", "", "", "")
}

// UserRoutine class representing workout routines
@IgnoreExtraProperties
data class UserRoutine(
    @get:PropertyName("routineID")
    @set:PropertyName("routineID")
    var routineID: String = "",

    @get:PropertyName("Name")
    @set:PropertyName("Name")
    var name: String = "",

    @get:PropertyName("Exercises")
    @set:PropertyName("Exercises")
    var exercises: Map<String, Exercise> = emptyMap()
) {
    constructor() : this("", "", emptyMap())
}


// UserWorkout class representing individual workouts
@IgnoreExtraProperties
data class UserWorkout(
    @get:PropertyName("userWorkoutID")
    @set:PropertyName("userWorkoutID")
    var userWorkoutID: String = "",

    @get:PropertyName("StartDateTime")
    @set:PropertyName("StartDateTime")
    var start: String = "",

    @get:PropertyName("EndDateTime")
    @set:PropertyName("EndDateTime")
    var end: String = "",

    @get:PropertyName("RoutineID")
    @set:PropertyName("RoutineID")
    var routineID: String = "",

    @get:PropertyName("WorkoutExercises")
    @set:PropertyName("WorkoutExercises")
    var workoutExercises: Map<String, WorkoutExercise> = emptyMap()
) {
    constructor() : this("", "", "", "", emptyMap())
}

// WorkoutExercise class representing individual exercises performed during a workout
@IgnoreExtraProperties
data class WorkoutExercise(
    @get:PropertyName("exerciseID")
    @set:PropertyName("exerciseID")
    var exerciseID: String = "",

    @get:PropertyName("ExerciseName")
    @set:PropertyName("ExerciseName")
    var exerciseName: String = "",

    @get:PropertyName("Category")
    @set:PropertyName("Category")
    var category: String = "",

    @get:PropertyName("Weight(kg)")
    @set:PropertyName("Weight(kg)")
    var weight: Int? = 0,

    @get:PropertyName("Reps")
    @set:PropertyName("Reps")
    var reps: Int? = 0,

    @get:PropertyName("Distance")
    @set:PropertyName("Distance")
    var distance: Int? = 0,

    @get:PropertyName("DurationSeconds")
    @set:PropertyName("DurationSeconds")
    var durationSeconds: Int? = 0,

    @get:PropertyName("Notes")
    @set:PropertyName("Notes")
    var notes: String = ""
) {
    constructor() : this("", "", "", 0, 0, 0, 0, "")
}

// Exercise class for storing default and custom exercises
@IgnoreExtraProperties
data class Exercise(
    @get:PropertyName("exercise_id")
    @set:PropertyName("exercise_id")
    var exerciseID: String = "",

    @get:PropertyName("ExerciseName")
    @set:PropertyName("ExerciseName")
    var exerciseName: String = "",

    @get:PropertyName("Category")
    @set:PropertyName("Category")
    var category: String = "",

    @get:PropertyName("Notes")
    @set:PropertyName("Notes")
    var notes: String = ""
) {
    constructor() : this("", "", "", "")
}

// UserBackground class for representing user backgrounds
@IgnoreExtraProperties
data class UserBackground(
    @get:PropertyName("BackgroundID")
    @set:PropertyName("BackgroundID")
    var backgroundID: String = "",

    @get:PropertyName("Name")
    @set:PropertyName("Name")
    var name: String = "",

    @get:PropertyName("BackgroundURI")
    @set:PropertyName("BackgroundURI")
    var backgroundURI: String = ""
) {
    constructor() : this("", "", "")
}

