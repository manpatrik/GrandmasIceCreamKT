package com.example.grandmasicecreamkt.di

import androidx.room.Room
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.CartF.CartViewModel
import com.example.grandmasicecreamkt.IceCreams.IceCreamsViewModel
import com.example.grandmasicecreamkt.network.APIClient
import com.example.grandmasicecreamkt.network.APIInterface
import com.example.grandmasicecreamkt.repositories.ExtrasRepository
import com.example.grandmasicecreamkt.repositories.IceCreamRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Cart()}
    single<ICartDAO>{ CartDatabase.getInstance(androidContext()).cartDao() }
}

val networkModule = module {
    single { APIClient() }
    single<APIInterface> { get<APIClient>().createApi() }
    single { IceCreamRepository(get()) }
    single { ExtrasRepository(get()) }
}

val viewModelModule = module {
    viewModel { IceCreamsViewModel(get(), get(), get()) }
    viewModel { CartViewModel(get(), get()) }
}