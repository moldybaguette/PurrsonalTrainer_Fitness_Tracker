package com.example.example

import com.google.gson.annotations.SerializedName

// for example "push day" or "calisthenics"
data class UserRoutine (

  @SerializedName("routine_id" ) var routineID : String   = "",
  @SerializedName("Name"      ) var name      : String   = "",
  @SerializedName("Exercises"  ) val exercises  : MutableMap<String,Exercise> = mutableMapOf()

)