package com.example.pokefight.domain.BDD.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DiscoveredPokemonBDD(
    var discovered : List<Int>,
    @PrimaryKey
    var idUser: Int? = null
)
