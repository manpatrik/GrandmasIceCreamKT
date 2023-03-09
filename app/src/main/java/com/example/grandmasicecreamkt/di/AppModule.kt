package com.example.grandmasicecreamkt.di

import androidx.room.Room
import com.example.grandmasicecreamkt.*
import com.example.grandmasicecreamkt.CartF.CartViewModel
import com.example.grandmasicecreamkt.IceCreams.IceCreamsViewModel
import com.example.grandmasicecreamkt.network.APIClient
import com.example.grandmasicecreamkt.network.APIInterface
import com.example.grandmasicecreamkt.repositories.CartRepository
import com.example.grandmasicecreamkt.repositories.ExtrasRepository
import com.example.grandmasicecreamkt.repositories.IceCreamRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Cart()}
}

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CartDatabase::class.java,
            "cart_database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addTypeConverter(CartConverters())
            .build()
    }
    single<ICartDAO> { get<CartDatabase>().cartDao() }
}

val networkModule = module {
    single { APIClient() }
    single<APIInterface> { get<APIClient>().createApi() }
}

val repositoryModule = module {
    single { IceCreamRepository(get()) }
    single { ExtrasRepository(get()) }
    single { CartRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { IceCreamsViewModel(get(), get()) }
    viewModel { CartViewModel(get(), get()) }
}