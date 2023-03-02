package com.example.grandmasicecreamkt.IceCreams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grandmasicecreamkt.Cart
import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.IceCream
import com.example.grandmasicecreamkt.network.IceCreamRepository
import kotlinx.coroutines.launch

class IceCreamsViewModel (
    val iceCreamRepository: IceCreamRepository,
    val cart: Cart
        ) : ViewModel() {

    private val _iceCreams: MutableLiveData<Resource<List<IceCream>>> = MutableLiveData()
    val iceCreams: LiveData<Resource<List<IceCream>>>
        get() = _iceCreams

    init {
        loadIceCreams()
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