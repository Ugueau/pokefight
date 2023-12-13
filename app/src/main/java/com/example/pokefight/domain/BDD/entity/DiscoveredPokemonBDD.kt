package com.example.pokefight.domain.BDD.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class DiscoveredPokemonBDD(
    var teams : List<Int>,
    @PrimaryKey
    var idUser: Int? = null
)