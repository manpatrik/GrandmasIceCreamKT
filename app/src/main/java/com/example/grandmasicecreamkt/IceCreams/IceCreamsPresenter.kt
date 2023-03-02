package com.example.grandmasicecreamkt

import android.content.res.Resources
import com.example.grandmasicecreamkt.IceCreams.IceCreamFragmentInterFace
import com.example.grandmasicecreamkt.IceCreams.IceCreamsPresenterInterface


class IceCreamsPresenter(
    override val view: IceCreamFragmentInterFace,
    val cart: Cart,
    val resources: Resources
    ) : IceCreamsPresenterInterface {


    override fun getIceCreams(): ArrayList<IceCream> {
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