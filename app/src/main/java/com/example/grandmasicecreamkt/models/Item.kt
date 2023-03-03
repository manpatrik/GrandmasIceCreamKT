package com.example.grandmasicecreamkt

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double
)