package com.example.grandmasicecreamkt

import android.content.res.Resources
import android.util.Log
import com.example.grandmasicecreamkt.APIClient.client
import com.example.grandmasicecreamkt.IceCreams.IceCreamFragmentInterFace
import com.example.grandmasicecreamkt.IceCreams.IceCreamsPresenterInterface
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


class IceCreamsPresenter(
    override val view: IceCreamFragmentInterFace,
    val cart: Cart,
    val resources: Resources
    ) : IceCreamsPresenterInterface {


    override fun getIceCreams(): ArrayList<IceCream> {
        val apiInterface = client?.create<APIInterface>(APIInterface::class.java)

        val call: Call<MultipleResource?>? = apiInterface?.doGetListResources()
        call?.enqueue(object : Callback<MultipleResource?> {
            override fun onResponse(call: Call<MultipleResource?>?, response: Response<MultipleResource?>) {
                Log.d("TAG", response.code().toString() + "")
                var displayResponse = ""
                val resource: MultipleResource? = response.body()
                val text: Int? = resource?.basePrice
                val iceCreamList: List<MultipleResource.IceCreams>? = resource?.iceCreams

                if (iceCreamList != null) {
                    for (iceCream in iceCreamList) {
                        displayResponse += iceCream.name + ", "
                    }
                }
                Log.d("ICECREAMS", displayResponse)
            }

            override fun onFailure(call: Call<MultipleResource?>, t: Throwable?) {
                call.cancel()
            }
        })

        val iceCreams = ArrayList<IceCream>()
        val iceCream = IceCream(1,"vanilia", IceCream.Status.AVAILABLE, "https://raw.githubusercontent.com/udemx/hr-resources/master/images/dio.jpg")
        iceCreams.add(iceCream)
        iceCreams.add(iceCream)
        iceCreams.add(iceCream)
        return iceCreams
    }

    override fun addCartItem(cartItem: CartItem) {
        cart.cartItems.add(cartItem)
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