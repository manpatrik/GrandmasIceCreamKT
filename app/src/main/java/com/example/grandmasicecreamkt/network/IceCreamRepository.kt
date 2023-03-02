package com.example.grandmasicecreamkt.network

import com.example.grandmasicecreamkt.IceCream
import com.example.grandmasicecreamkt.di.viewModelModule
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class IceCreamRepository(
    private val api: APIInterface
) {
    suspend fun loadIceCreams(): List<IceCream> {
        val result: LoadIcecreamsResponse = api.doGetListResources()
        return result.iceCreams?.map { it.toIceCream() }.orEmpty()
    }
}

fun LoadIcecreamsResponse.IceCreamsDTO.toIceCream() = this.run {
    IceCream(
        id = id ?: 0L,
        name = name.orEmpty(),
        status = status?.let { IceCream.Status.valueOf(it) } ?: IceCream.Status.UNAVAILABLE,
        imageUrl = imageUrl.orEmpty())
}

class APIClient() {
    private val retrofit: Retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun createApi() = retrofit.create(APIInterface::class.java)
}

interface APIInterface {
    @GET("/udemx/hr-resources/master/icecreams.json")
    suspend fun doGetListResources(): LoadIcecreamsResponse

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
        var status: String? = null

        @SerializedName("imageUrl")
        var imageUrl: String? = null
    }
}