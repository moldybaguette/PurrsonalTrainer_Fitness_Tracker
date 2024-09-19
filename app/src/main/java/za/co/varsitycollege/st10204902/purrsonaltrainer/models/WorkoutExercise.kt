package com.example.example

import com.google.gson.annotations.SerializedName

//this stores the exercise ID so having name and category here is somewhat redundant,
// but it could be convenient for displaying the exercise name in the UI
data class WorkoutExercise(
    // this will be the key for the userExercise node (so a foreign key)
    @SerializedName("ExerciseID") var exerciseID: String = "",
    @SerializedName("ExerciseName") var exerciseName: String = "",
    @SerializedName("Category") var category: String = "",
    @SerializedName("Weight(kg)") var weight: Int? = 0,
    @SerializedName("Reps") var reps: Int? = 0,
    @SerializedName("Distance") var distance: Int? = 0,
    @SerializedName("DurationSeconds") var durationSeconds: Int? = 0,
    @SerializedName("Notes") var notes: String = "",
)