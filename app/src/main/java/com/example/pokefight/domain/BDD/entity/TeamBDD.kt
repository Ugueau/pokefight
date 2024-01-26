package com.example.pokefight.domain.BDD.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TeamBDD(
    var teams : List<Int>,
    @PrimaryKey
    var idUser: Int? = null
)
