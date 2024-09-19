package com.example.example

import com.google.gson.annotations.SerializedName


data class UserBackground(

    @SerializedName("BackgroundID") var BackgroundID: String = "",
    @SerializedName("Name") var Name: String = "",
    @SerializedName("BackgroundURI") var BackgroundURI: String = ""

)