package com.example.grandmasicecreamkt.network

import com.example.grandmasicecreamkt.Extra
import com.example.grandmasicecreamkt.Item
import com.google.gson.annotations.SerializedName

class ExtrasRepository(
    private val api: APIInterface
) {
    suspend fun loadExtras(): List<Extra> {
        return api.doGetExtraResources()
    }
}