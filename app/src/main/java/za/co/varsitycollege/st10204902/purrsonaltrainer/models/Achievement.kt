package com.example.example
import com.google.gson.annotations.SerializedName

// this will be all the achievements available be unlocked by users
data class Achievement(
    //since there are a fixed number of achievements, we can use the achievement name as the key if we want but ive put both
    // properties here to keep it consistent with the other classes
    @SerializedName("achievement_id") var achievementID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("Description") var description: String = ""

)