package com.example.grandmasicecreamkt.repositories

import com.example.grandmasicecreamkt.IceCream
import com.example.grandmasicecreamkt.network.APIInterface
import com.google.gson.annotations.SerializedName


class IceCreamRepository(
    private val api: APIInterface
) {
    var cache: List<IceCream> = emptyList()

    suspend fun loadIceCreams(): List<IceCream> {
        cache.ifEmpty{
            val result: LoadIcecreamsResponse = api.doGetListResources()
            cache= result.iceCreams?.map { it.toIceCream() }.orEmpty()
            return cache
        }
        return cache
    }
}

fun LoadIcecreamsResponse.IceCreamsDTO.toIceCream() = this.run {
    IceCream(
        id = id ?: 0L,
        name = name.orEmpty(),
        status = status?: IceCream.Status.UNAVAILABLE,
        imageUrl = imageUrl.orEmpty())
}

class LoadIcecreamsResponse {
    @SerializedName("basePrice")
    var basePrice: Int? = null

    @SerializedName("iceCreams")
    var iceCreams: List<IceCreamsDTO>? = null

    inner class IceCreamsDTO {
        @SerializedName("id")
        var id: Long? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("status")
        var status: IceCream.Status? = null

        @SerializedName("imageUrl")
        var imageUrl: String? = null
    }
}