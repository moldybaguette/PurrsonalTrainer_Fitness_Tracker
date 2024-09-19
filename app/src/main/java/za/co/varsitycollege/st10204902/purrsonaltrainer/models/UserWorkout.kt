package com.example.example

import com.google.gson.annotations.SerializedName

//VITALLY IMPORTANT: The Date field is stored as a string in the format "yyyy-MM-dd'T'HH:mm:ss.SSSSX"
data class UserWorkout(

    @SerializedName("userWorkout_id") var userWorkoutID: String = "",
    //Date DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSX")

    @SerializedName("StartDateTime") var start: String = "",
    @SerializedName("EndDateTime") var end: String = "",
    @SerializedName("RoutineID") var routineID: String = "",
    @SerializedName("WorkoutExercises") val workoutExercises: MutableMap<String, WorkoutExercise> = mutableMapOf()

)

