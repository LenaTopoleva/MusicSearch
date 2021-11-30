package com.lenatopoleva.musicsearch

import android.app.Application
import com.lenatopoleva.musicsearch.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application, viewModelModule, navigation, splashActivity, mainActivity,
                albumsFragment, albumDetailsFragment, authFragment, registrationFragment))
        }
    }

}