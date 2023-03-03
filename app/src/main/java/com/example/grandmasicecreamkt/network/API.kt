package com.example.grandmasicecreamkt.network

import com.example.grandmasicecreamkt.Extra
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

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

    @GET("/udemx/hr-resources/master/extras.json")
    suspend fun doGetExtraResources(): List<Extra>

    @POST("POST")
    fun sendOrder(): Call<Unit>

}