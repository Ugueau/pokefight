package com.example.pokefight.domain.BDD

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.pokefight.domain.BDD.DAO.UserDAO
import com.example.pokefight.domain.BDD.DAO.DiscoveredPokemonDAO
import com.example.pokefight.domain.BDD.Converters
import com.example.pokefight.domain.BDD.DAO.TeamDAO
import com.example.pokefight.domain.BDD.entity.DiscoveredPokemonBDD
import com.example.pokefight.domain.BDD.entity.UserBDD

@Database(entities = [ UserBDD::class, DiscoveredPokemonBDD::class ], version = 1)
@TypeConverters(Converters::class)
abstract class PokefightBDD: RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun discoveredPokemonDAO(): DiscoveredPokemonDAO
    abstract fun teamDAO(): TeamDAO

    companion object{

        private lateinit var instance: PokefightBDD

        fun initDatabase(context: Context){
            instance = Room.databaseBuilder(
                context, PokefightBDD::class.java, "PokefightBDD"
            )
                .build()
        }

        fun getInstance(): PokefightBDD{
            return instance
        }

    }
}