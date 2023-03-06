package com.example.grandmasicecreamkt.network

import com.example.grandmasicecreamkt.Extra
import com.example.grandmasicecreamkt.Item
import com.google.gson.annotations.SerializedName

class ExtrasRepository(
    private val api: APIInterface
) {
    var cache: List<Extra> = emptyList()
    suspend fun loadExtras(): List<Extra> {
        cache.ifEmpty { cache = api.doGetExtraResources() }
        return cache
    }
}