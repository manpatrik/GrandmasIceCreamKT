package com.example.grandmasicecreamkt

class IceCream(var id: Long, var name: String, status: String, imageUrl: String) {
    var status: Status? = null
        private set
    val imageUrl: String

    enum class Status {
        AVAILABLE, MELTED, UNAVAILABLE
    }

    init {
        setStatus(status)
        this.imageUrl = imageUrl
    }

    fun setStatus(status: String) {
        when (status) {
            "available" -> this.status = Status.AVAILABLE
            "melted" -> this.status = Status.MELTED
            else -> this.status = Status.UNAVAILABLE
        }
    }
}