package com.example.pokefight.domain.BDD.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserBDD(

    var Email: String,
    var Nickname: String,
    var trophy: Int,
    var pokedollar: Int,
    var UserToken: String,
    var Password: String
){
    @PrimaryKey(autoGenerate = true)
    var idUser: Int? = null
}
