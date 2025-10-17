package com.example.pokefight.domain

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pokefight.domain.preferences.PreferencesDataSource
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalDateTime

object BoutiqueRepository {

    private val ID1_KEY = intPreferencesKey("draw_id_1")
    private val ID2_KEY = intPreferencesKey("draw_id_2")
    private val ID3_KEY = intPreferencesKey("draw_id_3")
    private val DATE_KEY = stringPreferencesKey("draw_date")

    suspend fun saveTodayDraws(id1: Int, id2: Int, id3: Int) {
        val now = LocalDateTime.now()
        PreferencesDataSource.save(ID1_KEY, id1)
        PreferencesDataSource.save(ID2_KEY, id2)
        PreferencesDataSource.save(ID3_KEY, id3)
        PreferencesDataSource.saveDate(DATE_KEY, now)
    }

    suspend fun getLastDraws(): BoutiqueDraws {
        val id1 = PreferencesDataSource.read(ID1_KEY, -1).firstOrNull() ?: 1 // Default return base pokemon
        val id2 = PreferencesDataSource.read(ID2_KEY, -1).firstOrNull() ?: 4
        val id3 = PreferencesDataSource.read(ID3_KEY, -1).firstOrNull() ?: 7
        val date = PreferencesDataSource.readDate(DATE_KEY).firstOrNull() ?: LocalDateTime.now().minusDays(1) // Yesterday date to force VM to re draw new pokemon

        return BoutiqueDraws(id1, id2, id3, date)
    }
}

data class BoutiqueDraws(
    val id1: Int,
    val id2: Int,
    val id3: Int,
    val savedDate: LocalDateTime
)
