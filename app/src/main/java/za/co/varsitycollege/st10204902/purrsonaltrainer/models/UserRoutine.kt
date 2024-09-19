package com.example.example

import com.google.gson.annotations.SerializedName
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise

// for example "push day" or "calisthenics"
data class UserRoutine (

  @SerializedName("routine_id" ) var routineID : String   = "",
  @SerializedName("Name"      ) var name      : String   = "",
  @SerializedName("Exercises"  ) val exercises  : MutableMap<String, Exercise> = mutableMapOf()

)