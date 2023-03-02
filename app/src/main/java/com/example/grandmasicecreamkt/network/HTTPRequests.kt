package com.example.grandmasicecreamkt.network

import android.util.Log
import com.example.grandmasicecreamkt.IceCream
import com.example.grandmasicecreamkt.network.APIClient.client
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class HTTPRequests {
    fun loadIceCreams(): List<IceCream> {
//        var iceCreams: List<IceCream> = ArrayList()
        val apiInterface = client.create<APIInterface>(APIInterface::class.java)

        val call: Call<MultipleResource?> = apiInterface.doGetListResources()
        call.execute()


//        call?.enqueue(object : Callback<MultipleResource?> {
//            override fun onResponse(
//                call: Call<MultipleResource?>?,
//                response: Response<MultipleResource?>
//            ) {
//                val resource: MultipleResource? = response.body()
//                val text: Int? = resource?.basePrice
//                val iceCreamList: List<MultipleResource.IceCreams>? = resource?.iceCreams
//
//                if (iceCreamList != null) {
//                    for (iceCream in iceCreamList) {
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<MultipleResource?>?, t: Throwable?) {
//                TODO("Not yet implemented")
//            }
//        })
        val iceCreams = ArrayList<IceCream>()
        val iceCream = IceCream(1,"vanilia", IceCream.Status.AVAILABLE, "https://raw.githubusercontent.com/udemx/hr-resources/master/images/dio.jpg")
        iceCreams.add(iceCream)
        iceCreams.add(iceCream)
        iceCreams.add(iceCream)
        return iceCreams
    }
}

internal object APIClient {
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            retrofit = Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
}

interface APIInterface {
    @GET("/udemx/hr-resources/master/icecreams.json")
    fun doGetListResources(): Call<MultipleResource?>?
}

class MultipleResource {
    @SerializedName("basePrice")
    var basePrice: Int? = null

    @SerializedName("iceCreams")
    var iceCreams: List<IceCreams>? = null

    inner class IceCreams {
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

