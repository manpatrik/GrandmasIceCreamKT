package com.example.grandmasicecreamkt

import android.content.res.Resources
import android.view.View
import org.koin.java.KoinJavaComponent.inject

interface IceCreamsPresenterInterface {
    fun getIceCreams(): ArrayList<IceCream>
    fun addCartItem(cartItem: CartItem)
}

class IceCreamsPresenter(val cart: Cart, val resources: Resources) : IceCreamsPresenterInterface {


    override fun getIceCreams(): ArrayList<IceCream> {
        //val iceCreamsFromJSON = JSONLoader.loadIceCreamsFromJson()

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