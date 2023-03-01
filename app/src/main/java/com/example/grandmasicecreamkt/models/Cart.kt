package com.example.grandmasicecreamkt

import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.Extra
import com.example.grandmasicecreamkt.IceCream

class Cart {
    private val iceCreams = mutableListOf<IceCream>()
    var extras = mutableListOf<Extra>()
    var cartItems = mutableListOf<CartItem>()
    var requiredExtraFirstItemIds = mutableListOf<Long>()
}