package com.example.pokefight

import android.app.Application
import com.example.pokefight.domain.BDD.PokefightBDD
import timber.log.Timber

class PokefightApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        PokefightBDD.initDatabase(context = applicationContext)
    }

}