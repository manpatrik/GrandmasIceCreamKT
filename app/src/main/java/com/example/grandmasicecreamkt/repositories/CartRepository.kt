package com.example.grandmasicecreamkt.repositories

import android.widget.RemoteViews.RemoteCollectionItems
import com.example.grandmasicecreamkt.Cart
import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.ICartDAO

class CartRepository(
    private val iceCreamRepository: IceCreamRepository,
    private val dao: ICartDAO
) {
    var cartItems: MutableList<CartItem> = mutableListOf<CartItem>()

    suspend fun loadCartItems() {
        val cartEntities = dao.loadAllCarts()
        var cartItems: MutableList<CartItem> = mutableListOf()
        val iceCreams = iceCreamRepository.loadIceCreams()
        cartEntities.forEach {cartEntities ->
            iceCreams.find { it.id == cartEntities.iceCreamId }?.let { iceCream ->
                cartItems.add(CartItem(cartEntities.id, iceCream, cartEntities.extraIds)) }
        }
        this.cartItems = cartItems
    }

    fun removeCartItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
        dao.deleteCartItem(cartItem.toCartEntity())
    }

    fun addCartItem(cartItem: CartItem) {
        cartItems.add(cartItem)
        dao.insertCartItem(cartItem.toCartEntity())
    }

    fun updateCartItem(cartItem: CartItem) {
        dao.updateCartItem(cartItem.toCartEntity())
    }


}