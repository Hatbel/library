package com.example.library

import android.app.Application
import com.example.library.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule, dataBaseModule, viewModelsModule, networkModule,
                    repositoriesModel
                )
            )
        }
    }

}