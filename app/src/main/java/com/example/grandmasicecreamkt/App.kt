package com.example.grandmasicecreamkt

import android.app.Application
import com.example.grandmasicecreamkt.di.appModule
import com.example.grandmasicecreamkt.di.networkModule
import com.example.grandmasicecreamkt.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, viewModelModule, networkModule)
        }
    }
}