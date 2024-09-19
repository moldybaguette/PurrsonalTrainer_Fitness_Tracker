package com.example.example

import com.google.gson.annotations.SerializedName

// when a user unlocks an achievement, it will be stored here. thus no need for a "unlocked" boolean
data class UserAchievement(

    @SerializedName("AchievementID") var achievementID: String = "",
    //having name here is somewhat redundant, but it could be convenient for displaying the achievement name in the UI
    @SerializedName("Name") var name: String = "",
    @SerializedName("CatImageURI") var CatImageURI: String = "",
    @SerializedName("CatFact") var catFact: String = ""

)