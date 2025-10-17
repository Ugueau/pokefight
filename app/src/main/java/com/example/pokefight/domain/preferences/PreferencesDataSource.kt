package com.example.pokefight.domain.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PreferencesDataSource {

    private lateinit var appContext: Context
    private val Context.internalDataStore by preferencesDataStore(name = "app_preferences")
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun init(context: Context) {
        if (!::appContext.isInitialized) {
            appContext = context.applicationContext
        }
    }

    private val dataStore: DataStore<Preferences>
        get() {
            if (!::appContext.isInitialized) {
                throw IllegalStateException("PreferencesDataSource not initialized. Call PreferencesDataSource.init(context) in your Application class.")
            }
            return appContext.internalDataStore
        }

    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    fun <T> read(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStore.data.map { it[key] ?: default }

    suspend fun saveDate(key: Preferences.Key<String>, value: LocalDateTime) {
        dataStore.edit { it[key] = value.format(formatter) }
    }

    fun readDate(key: Preferences.Key<String>): Flow<LocalDateTime?> =
        dataStore.data.map { it[key]?.let { LocalDateTime.parse(it, formatter) } }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}

