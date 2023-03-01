package com.example.grandmasicecreamkt.di

import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.IceCreams.IceCreamFragmentInterFace
import com.example.grandmasicecreamkt.IceCreams.IceCreamsPresenterInterface
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    factory<IceCreamsPresenterInterface> { (view: IceCreamFragmentInterFace) -> IceCreamsPresenter(view, get(), get()) }
    factory<CartPresenterInterface> { CartPresenter(get()) }
    single { Cart() }
    factory { androidContext().resources }
}