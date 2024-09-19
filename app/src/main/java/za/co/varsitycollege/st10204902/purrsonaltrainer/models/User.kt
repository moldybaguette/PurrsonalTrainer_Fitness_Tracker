package com.example.example

import com.google.gson.annotations.SerializedName
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Item
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserAchievement
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserBackground
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout

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