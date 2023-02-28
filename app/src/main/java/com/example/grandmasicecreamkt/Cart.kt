package com.example.grandmasicecreamkt

class Cart {
    private val iceCreams = mutableListOf<IceCream>()
    var extras = mutableListOf<Extra>()
    var cartItems = mutableListOf<CartItem>()
    var requiredExtraFirstItemIds = mutableListOf<Long>()
}