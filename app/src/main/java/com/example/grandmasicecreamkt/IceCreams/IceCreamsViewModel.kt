package com.example.grandmasicecreamkt.IceCreams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grandmasicecreamkt.IceCream
import com.example.grandmasicecreamkt.network.HTTPRequests
import kotlinx.coroutines.launch

class IceCreamsViewModel (
    val httpRequests: HTTPRequests
        ) : ViewModel() {

    private val _iceCreams: MutableLiveData<List<IceCream>> = MutableLiveData()
    val iceCreams: LiveData<List<IceCream>>
        get() = _iceCreams

    init {
        loadIceCreams()
    }

    private fun loadIceCreams() {
        viewModelScope.launch {
            _iceCreams.value = httpRequests.loadIceCreams()
        }
    }
}