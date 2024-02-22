package com.example.pokefight.domain.BDD.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pokefight.domain.BDD.entity.DiscoveredPokemonBDD

@Dao
interface DiscoveredPokemonDAO {
    @Insert
    suspend fun insertDiscoveredPokemon(discoveredPokemonBDD: DiscoveredPokemonBDD)

    @Delete
    suspend fun deleteUser(discoveredPokemonBDD: DiscoveredPokemonBDD)

    @Query("SELECT * FROM DiscoveredPokemonBDD WHERE idUser = :id LIMIT 1")
    suspend fun findDiscoveredPokemonFromUserId(id: Int): DiscoveredPokemonBDD?

    @Update
    suspend fun updateDiscoveredPokemon(discoveredPokemonBDD: DiscoveredPokemonBDD)
}