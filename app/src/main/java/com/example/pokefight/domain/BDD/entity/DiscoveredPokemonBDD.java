package com.example.pokefight.domain.BDD.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class DiscoveredPokemonBDD (


){
    @PrimaryKey(autoGenerate = true)
    var idUser: Int? = null
}
