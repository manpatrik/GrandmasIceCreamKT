package com.example.grandmasicecreamkt

interface CartPresenterInterface{
    fun getCartItems(): List<CartItem>
    fun removeCartItem(cartItem: CartItem)
    fun isCartItemContainExtraItem(cartItem: CartItem?, id: Long): Boolean
    fun getExtras(): List<Extra>

}

class CartPresenter(private val cart: Cart): CartPresenterInterface {
    override fun getCartItems(): List<CartItem> {
        return cart.cartItems
    }

    override fun removeCartItem(cartItem: CartItem) {
        cart.cartItems.remove(cartItem)
    }

    override fun isCartItemContainExtraItem(cartItem: CartItem?, id: Long): Boolean {
        // IceCreamsAtivity.cartItems.get(IceCreamsAtivity.cartItems.indexOf(cartItem))
        //                    .getExtraItemIds().contains(item.id)
        val cartItemIndex = cart.cartItems.indexOf(cartItem)
        return cart.cartItems.get(cartItemIndex).extraItemIds.contains(id)
    }

    override fun getExtras(): List<Extra> {
        return cart.extras
    }

}