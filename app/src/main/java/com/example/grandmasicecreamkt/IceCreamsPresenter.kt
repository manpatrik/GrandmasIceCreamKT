package com.example.grandmasicecreamkt

interface IceCreamsPresenterInterface {
    fun proba(str: String): String
}

class IceCreamsPresenter() : IceCreamsPresenterInterface {
    override fun proba(str: String): String {
        return str
    }

}