package com.example.grandmasicecreamkt.CartF

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grandmasicecreamkt.Cart
import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.Extra
import com.example.grandmasicecreamkt.IceCream

class CartViewModel(val cart: Cart): ViewModel() {

    private val _cartItems: MutableLiveData<List<CartItem>> = MutableLiveData()
    val cartItems: LiveData<List<CartItem>>
        get() = _cartItems

    init {
        getCartItems()
    }

    private fun getCartItems() {
        _cartItems.value = cart.cartItems
    }

    fun removeCartItem(cartItem: CartItem) {
        cart.cartItems.remove(cartItem)
    }

    fun isCartItemContainExtraItem(cartItem: CartItem?, id: Long): Boolean {
//        cart.cartItems.find { it == cartItem }?.let { return cartItem?.extraItemIds?.contains(id) == true }
//        return false
        val cartItemIndex = cart.cartItems.indexOf(cartItem)
        return cart.cartItems.get(cartItemIndex).extraItemIds.contains(id)
    }

    fun getExtras(): List<Extra> {
        return cart.extras
    }
}