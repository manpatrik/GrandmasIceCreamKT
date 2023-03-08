package com.example.grandmasicecreamkt.CartF

import androidx.lifecycle.*
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.repositories.CartRepository
import com.example.grandmasicecreamkt.repositories.ExtrasRepository
import kotlinx.coroutines.launch

class CartViewModel(
    val cart: Cart,
    val extrasRepository : ExtrasRepository,
    private val cartRepository: CartRepository
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
        setCartItems()
        getExtras()
    }

    private fun setCartItems() {
        _cartItems.value = cart.cartItems
    }

    fun getExtras(){
        viewModelScope.launch {
            val extras = extrasRepository.loadExtras()
            _extras.value = extras
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        cartRepository.removeCartItem(cartItem)
        setCartItems()
    }

    fun addOrRemoveExtraIdfromCart(cartItem: CartItem, id: Long, checked: Boolean) {
        cart.cartItems.find { it == cartItem }.let {
            it?.addOrRemoveExtraId(id, checked)
            cartRepository.updateCartItem(cartItem)
        }
    }

    fun changeExpandedStatus(cartItem: CartItem) {
        cart.cartItems.find { it.expanded }?.let {
            if (cartItem != it){
                it.changeExpandedStatus()
            }
        }

        cart.cartItems.find { it == cartItem }.let {
            it?.changeExpandedStatus()
            setCartItems()
        }
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