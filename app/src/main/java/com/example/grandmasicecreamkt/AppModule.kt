package com.example.grandmasicecreamkt

import org.koin.dsl.module

val appModule = module {
    factory<IceCreamsPresenterInterface> { IceCreamsPresenter() }
}