package com.example.grandmasicecreamkt

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    factory<IceCreamsPresenterInterface> { IceCreamsPresenter(get(), get())}
    factory<CartPresenterInterface> { CartPresenter(get()) }
    single { Cart() }
    factory { androidContext().resources }
}

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}