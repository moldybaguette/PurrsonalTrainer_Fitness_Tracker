package com.example.example

import com.google.gson.annotations.SerializedName

// these will be hardcoded as there will only be 5 items in the shop. again item id can just be the item name
data class Item(

    @SerializedName("item_id") var itemID: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("Description") var description: String = "",
    @SerializedName("Cost") var cost: Int = 0,
    @SerializedName("ItemURI") var itemURI: String = ""

)