package com.example.grandmasicecreamkt.IceCreams

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.repositories.CartRepository
import com.example.grandmasicecreamkt.repositories.IceCreamRepository
import kotlinx.coroutines.launch

class IceCreamsViewModel (
    private val iceCreamRepository: IceCreamRepository,
    private val cartRepository: CartRepository
        ) : ViewModel() {

    val iceCreams: MutableState<List<IceCream>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            cartRepository.loadCartItems()
        }
        loadIceCreams()
    }

    private fun loadIceCreams() {
        viewModelScope.launch {
            iceCreams.value = iceCreamRepository.loadIceCreams()
        }
    }

    fun addCartItem(cartItem: CartItem) {
        cartRepository.addCartItem(cartItem)
    }
}

sealed interface Resource<T> {
    class Success<T>(val data: T) : Resource<T>
    class Loading<T>() : Resource<T>
}