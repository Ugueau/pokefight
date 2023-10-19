package com.example.pokefight

import android.app.Application
import timber.log.Timber

class PokefightApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

}