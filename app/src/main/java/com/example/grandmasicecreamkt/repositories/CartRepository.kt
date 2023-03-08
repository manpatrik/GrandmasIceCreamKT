package com.example.grandmasicecreamkt.repositories

import com.example.grandmasicecreamkt.Cart
import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.ICartDAO

class CartRepository(
    private val iceCreamRepository: IceCreamRepository,
    private val dao: ICartDAO,
    private val cart: Cart
) {
//    val _cartItems: MutableLiveData<List<CartItem>> = MutableLiveData()

    suspend fun getCartItems(): MutableList<CartItem> {
        val cartEntities = dao.loadAllCarts()
        var cartItems: MutableList<CartItem> = mutableListOf()
        val iceCreams = iceCreamRepository.loadIceCreams()
        cartEntities.forEach {cartEntities ->
            iceCreams.find { it.id == cartEntities.iceCreamId }?.let { iceCream ->
                cartItems.add(CartItem(cartEntities.id, iceCream, cartEntities.extraIds)) }
        }
        return cartItems
    }

    fun removeCartItem(cartItem: CartItem) {
        cart.cartItems.remove(cartItem)
        dao.deleteCartItem(cartItem.toCartEntity())
    }

    fun addCartItem(cartItem: CartItem) {
        cart.cartItems.add(cartItem)
        dao.insertCartItem(cartItem.toCartEntity())
    }

    fun updateCartItem(cartItem: CartItem) {
        dao.updateCartItem(cartItem.toCartEntity())
    }


}