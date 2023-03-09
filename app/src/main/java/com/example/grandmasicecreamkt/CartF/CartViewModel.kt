package com.example.grandmasicecreamkt.CartF

import androidx.lifecycle.*
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.repositories.CartRepository
import com.example.grandmasicecreamkt.repositories.ExtrasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CartViewModel(
    private val extrasRepository : ExtrasRepository,
    private val cartRepository: CartRepository
): ViewModel() {

//    private val _cartItems: MutableLiveData<List<CartItem>> = MutableLiveData()
//    val cartItems: LiveData<List<CartItem>>
//        get() = _cartItems

//    private val _extras: MutableLiveData<List<Extra>> = MutableLiveData()
//    val extras: LiveData<List<Extra>>
//        get() = _extras
//
//    val cartData: LiveData<CartData> = _cartItems.combineWith(_extras) { cartItems, extras->
//        CartData(cartItems.orEmpty(), extras.orEmpty())
//    }

    private val _cartItems: MutableStateFlow<List<CartItem>> = MutableStateFlow(listOf())
    private val _extras: MutableStateFlow<List<Extra>> = MutableStateFlow(listOf())

//    private val _cartData: MutableStateFlow<CartData> = _cartItems.combine(_extras) { cartItems, extras->
//        CartData(cartItems.orEmpty(), extras.orEmpty())
//    }
//    val cartData: MutableStateFlow<CartData> get() = _cartData


    init {
        setCartItems()
        getExtras()
    }

    private fun setCartItems() {
        _cartItems.value = cartRepository.cartItems
    }

    fun getExtras(){
        viewModelScope.launch {
            _extras.value = extrasRepository.loadExtras()
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        cartRepository.removeCartItem(cartItem)
        setCartItems()
    }

    fun addOrRemoveExtraIdfromCart(cartItem: CartItem, extraId: Long, checked: Boolean) {
        cartRepository.cartItems.find { it.id == cartItem.id }.let {
            it?.addOrRemoveExtraId(extraId, checked)
            cartRepository.updateCartItem(cartItem)
        }
    }

    fun changeExpandedStatus(cartItem: CartItem) {
        cartRepository.cartItems.find { it.expanded }?.let {
            if (cartItem != it){
                it.changeExpandedStatus()
            }
        }

        cartRepository.cartItems.find { it == cartItem }.let {
            it?.changeExpandedStatus()
            setCartItems()
        }
    }
}
//fun <T1, T2, R> LiveData<T1>.combineWith(
//    liveData: LiveData<T2>,
//    block: (T1?, T2?) -> R
//): LiveData<R> {
//    val result = MediatorLiveData<R>()
//    result.addSource(this) {
//        result.value = block(this.value, liveData.value)
//    }
//    result.addSource(liveData) {
//        result.value = block(this.value, liveData.value)
//    }
//    return result
//}

class CartData( val cartItems: List<CartItem>, val extras: List<Extra>)