package com.example.grandmasicecreamkt

import android.view.View

interface IceCreamsPresenterInterface {
    fun getIceCreams(): ArrayList<IceCream>
}

class IceCreamsPresenter() : IceCreamsPresenterInterface {
    override fun getIceCreams(): ArrayList<IceCream> {
        //val iceCreamsFromJSON = JSONLoader.loadIceCreamsFromJson()

        val iceCreams = ArrayList<IceCream>()
        val iceCream = IceCream(1,"vanilia", IceCream.Status.AVAILABLE, "")
        iceCreams.add(iceCream)
        iceCreams.add(iceCream)
        iceCreams.add(iceCream)
        return iceCreams
    }


}