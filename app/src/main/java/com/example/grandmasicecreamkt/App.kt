package com.example.grandmasicecreamkt

import android.app.Application
import com.example.grandmasicecreamkt.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                viewModelModule,
                networkModule,
                dbModule,
                repositoryModule
            )
        }
    }
}