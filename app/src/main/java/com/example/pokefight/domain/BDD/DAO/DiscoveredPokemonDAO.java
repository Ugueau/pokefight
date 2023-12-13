package com.example.pokefight.domain.BDD.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
interface DiscoveredPokemonDAO {
    @Insert
    suspend fun insertDiscoveredPokemon(userBDD: UserBDD)

    @Delete
    suspend fun deleteDiscoveredPokemon(userBDD: UserBDD)

    @Query("SELECT * FROM UserBDD WHERE email = :email LIMIT 1")
    suspend fun findDiscoveredPokemon(email: String): UserBDD?

    @Query("Select * from UserBDD where email = :email and password = :password")
    suspend fun findDiscoveredPokemon(email: String, password: String): UserBDD?
}
