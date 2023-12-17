package com.example.pokefight.domain.BDD

import android.content.Context
import androidx.room.*
import com.example.pokefight.domain.BDD.DAO.UserDAO
import com.example.pokefight.domain.BDD.entity.UserBDD

@Database(entities = [ UserBDD::class ], version = 1)
abstract class PokefightBDD: RoomDatabase() {

    abstract fun userDAO(): UserDAO

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