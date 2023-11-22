package com.example.pokefight.domain.BDD.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserBDD(
    @PrimaryKey(autoGenerate = true)
    val idUser: Int,
    var Email: String,
    var Nickname: String,
    var trophy: Int,
    var UserToken: String,
    var Password: String
)
