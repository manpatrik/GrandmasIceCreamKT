package com.example.grandmasicecreamkt.IceCreams

import com.example.grandmasicecreamkt.CartItem
import com.example.grandmasicecreamkt.IceCream


interface IceCreamsPresenterInterface {
    val view: IceCreamFragmentInterFace
    fun getIceCreams(): ArrayList<IceCream>
    fun addCartItem(cartItem: CartItem)
}

interface IceCreamFragmentInterFace{

}
