package com.example.pokefight.domain

import com.example.pokefight.domain.firebase.DSRealTimeDatabase

object FightRepository {

    fun askFight(tokenOpponent : String, tokenUser: String){
        DSRealTimeDatabase.askFight(tokenOpponent,tokenUser)
    }

    fun cancelFight(tokenOpponent: String){
        DSRealTimeDatabase.cancelFight(tokenOpponent)
    }
}