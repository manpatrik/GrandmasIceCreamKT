package com.example.grandmasicecreamkt

class IceCream(var id: Long, var name: String, var status: Status, var imageUrl: String) {

    enum class Status {
        AVAILABLE, MELTED, UNAVAILABLE
    }
}