package com.example.pokefight

import android.app.Application
import com.example.pokefight.domain.BDD.PokefightBDD
import com.example.pokefight.domain.firebase.DSFireStore
import com.example.pokefight.domain.firebase.DSRealTimeDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class PokefightApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        PokefightBDD.initDatabase(context = applicationContext)
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance()
        DSFireStore.startFireStoreConnection()
        DSRealTimeDatabase.startRealTimeConnection()
    }

}