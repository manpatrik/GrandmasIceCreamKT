package com.example.grandmasicecreamkt.repositories

import com.example.grandmasicecreamkt.Extra
import com.example.grandmasicecreamkt.network.APIInterface

class ExtrasRepository(
    private val api: APIInterface
) {
    var cache: List<Extra> = emptyList()
    suspend fun loadExtras(): List<Extra> {
        cache.ifEmpty { cache = api.doGetExtraResources() }
        return cache
    }
}