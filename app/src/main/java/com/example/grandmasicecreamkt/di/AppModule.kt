package com.example.grandmasicecreamkt.di

import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.IceCreams.IceCreamFragmentInterFace
import com.example.grandmasicecreamkt.IceCreams.IceCreamsPresenterInterface
import com.example.grandmasicecreamkt.IceCreams.IceCreamsViewModel
import com.example.grandmasicecreamkt.network.HTTPRequests
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory<IceCreamsPresenterInterface> { (view: IceCreamFragmentInterFace) -> IceCreamsPresenter(view, get(), get()) }
    factory<CartPresenterInterface> { CartPresenter(get()) }
    single { Cart() }
    factory { androidContext().resources }
}

val networkModule = module {
    single { HTTPRequests() }
}

val viewModelModule = module {
    viewModel { IceCreamsViewModel(get()) }
}