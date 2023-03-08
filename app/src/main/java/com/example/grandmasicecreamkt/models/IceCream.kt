package com.example.grandmasicecreamkt

import com.google.gson.annotations.SerializedName

class IceCream(var id: Long, var name: String, var status: Status, var imageUrl: String) {

    enum class Status {
        @SerializedName("available")
        AVAILABLE,
        @SerializedName("melted")
        MELTED,
        @SerializedName("unavailable")
        UNAVAILABLE
    }
}