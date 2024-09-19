package com.example.example

import com.google.gson.annotations.SerializedName

// used to store the default excercises offline to seed when the user creates their account.
// this will ALSO be user in the UserExcersise node to store the custom excercises the user creates.
data class Exercise(

    @SerializedName("exercise_id") var exerciseID: String = "",
    @SerializedName("ExerciseName") var exerciseName: String = "",
    //e.g "chest" , "back" , "legs"
    @SerializedName("Category") var category: String = ""

)