package com.example.grandmasicecreamkt

import com.google.gson.annotations.SerializedName

data class Extra(
    @SerializedName("type")
    val type: String,
    @SerializedName("required")
    val required: Boolean,
    @SerializedName("items")
    val items: MutableList<Item>
)