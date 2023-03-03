package com.example.grandmasicecreamkt.CartF

import android.util.Log
import androidx.lifecycle.*
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.network.ExtrasRepository
import kotlinx.coroutines.launch

class CartViewModel(
    val cart: Cart,
    val extrasRepository : ExtrasRepository
): ViewModel() {

    private val _cartItems: MutableLiveData<List<CartItem>> = MutableLiveData()
    val cartItems: LiveData<List<CartItem>>
        get() = _cartItems

    private val _extras: MutableLiveData<List<Extra>> = MutableLiveData()
    val extras: LiveData<List<Extra>>
        get() = _extras

    val cartData: LiveData<CartData> = _cartItems.combineWith(_extras) { cartItems, extras->
        CartData(cartItems.orEmpty(), extras.orEmpty())
    }

    init {
        getCartItems()
        getExtras()
    }

    private fun getCartItems() {
        _cartItems.value = cart.cartItems
    }

    fun getExtras(){
        viewModelScope.launch {
            _extras.value = extrasRepository.loadExtras()
        }
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
}
fun <T1, T2, R> LiveData<T1>.combineWith(
    liveData: LiveData<T2>,
    block: (T1?, T2?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}

class CartData( val cartItems: List<CartItem>, val extras: List<Extra>)