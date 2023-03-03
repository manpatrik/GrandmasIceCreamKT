package com.example.grandmasicecreamkt.network

import com.example.grandmasicecreamkt.Extra
import com.example.grandmasicecreamkt.Item
import com.google.gson.annotations.SerializedName

class ExtrasRepository(
    private val api: APIInterface
) {
    suspend fun loadExtras(): List<Extra> {
        val result: LoadExtrasResponse = api.doGetExtraResources()
        var extras = result.extras?.map { it.toExtra() }.orEmpty()
        return extras
    }
}

fun LoadExtrasResponse.ExtraDTO.toExtra() = this.run {
    val items = this.items?.map { it.toExtraItem() }.orEmpty()
    Extra(this.type ?: "", this.required ?: false, items)
}

fun LoadExtrasResponse.ExtraItemDTO.toExtraItem() = this.run {
    Item(this.id ?: 0L, this.name ?: "",this.price ?: 0.0)
}


class LoadExtrasResponse {
    @SerializedName("")
    var extras: List<ExtraDTO>? = null

    inner class ExtraDTO {
        @SerializedName("required")
        var required: Boolean? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("items")
        var items: List<ExtraItemDTO>? = null
    }

    inner class ExtraItemDTO {
        @SerializedName("price")
        var price: Double? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("id")
        var id: Long? = null
    }
}
