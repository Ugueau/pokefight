package com.example.pokefight.domain.BDD.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokefight.domain.BDD.entity.DiscoveredPokemonBDD
import com.example.pokefight.domain.BDD.entity.TeamBDD

@Dao
interface TeamDAO {
    @Insert
    suspend fun insertTeam(teamBDD: TeamBDD)

    @Delete
    suspend fun deleteTeam(teamBDD: TeamBDD)

    @Query("SELECT * FROM TeamBDD WHERE idUser = :id LIMIT 1")
    suspend fun findTeamFromUserId(id: Int): TeamBDD?
}