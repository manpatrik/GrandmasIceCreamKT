package com.example.grandmasicecreamkt.IceCreams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.repositories.IceCreamRepository
import kotlinx.coroutines.launch

class IceCreamsViewModel (
    private val iceCreamRepository: IceCreamRepository,
    private val cart: Cart,
    private val db: ICartDAO
        ) : ViewModel() {

    private val _iceCreams: MutableLiveData<Resource<List<IceCream>>> = MutableLiveData()
    val iceCreams: LiveData<Resource<List<IceCream>>>
        get() = _iceCreams

    init {
        loadIceCreams()

        db.insertCartItem(CartEntity(1L, mutableListOf(1L,2L,3L)))
        db.insertCartItem(CartEntity(2L, mutableListOf(4L,5L,6L)))
        println("alma "+db.loadAllCarts().size)
        db.insertCartItem(CartEntity(3L, mutableListOf(4L,5L,6L)))
        println("alma "+db.loadAllCarts().size)
    }

    private fun loadIceCreams() {
        viewModelScope.launch {
            _iceCreams.value = Resource.Loading()
            val iceCreams = iceCreamRepository.loadIceCreams()
            _iceCreams.value = Resource.Success(iceCreams)
        }
    }

    fun addCartItem(cartItem: CartItem) {
        cart.cartItems.add(cartItem)
    }
}

sealed interface Resource<T> {
    class Success<T>(val data: T) : Resource<T>
    class Loading<T>() : Resource<T>
}