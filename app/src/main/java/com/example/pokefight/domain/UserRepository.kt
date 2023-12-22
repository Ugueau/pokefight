package com.example.pokefight.domain

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import com.example.pokefight.domain.BDD.BDDDataSource
import com.example.pokefight.domain.BDD.entity.UserBDD
import com.example.pokefight.domain.api.DSRetrofit
import com.example.pokefight.domain.cache.DSPokemonCache
import com.example.pokefight.domain.cache.UserCache
import com.example.pokefight.domain.firebase.DSFireStore
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {

    suspend fun userExist(userToken: String): User?{
        return BDDDataSource.UserExistFromToken(userToken)
    }

    suspend fun insertUser(user : User): Boolean{
        var toReturn: Boolean = false
        toReturn = BDDDataSource.insertUser(user)
        if(toReturn){
            BDDDataSource.getUserFromEmail(user.Email)?.let{
                Log.e("uid2", it.UserToken)
                insertIntoFirestore(it)
            }
        }
        return toReturn
    }

    fun getUser(): User {
        var connectedUser = UserCache.getUser()

        if (connectedUser != null) {
            return connectedUser
        }

        return User("", "", "", 0 , 0,"",-1)
    }

    suspend fun connectUser(connectedUser: User){
        UserCache.addToCache(connectedUser)
    }

    suspend fun getDiscoveredPokemon(): List<Int>{
        val userId = UserCache.getUser()?.userId
        userId?.let {
            return BDDDataSource.getDiscoveredPokemonFromUserId(userId)
        }
        return emptyList()
    }

    suspend fun getTeam(): List<Pokemon>{
        val userId = UserCache.getUser()?.userId
        userId?.let {
            val pokemonTeam = mutableListOf<Pokemon>()
            BDDDataSource.getTeamFromUserId(userId).forEach {id ->
                PokemonRepository.getPokemonById(id)?.let {pokemon ->
                    pokemonTeam.add(pokemon)
                }
            }
            return pokemonTeam
        }
        return emptyList()
    }

    suspend fun updateTeam(newPokemonId : Int, oldPokemon : Int){
        val user = getUser()
        val teamToUpdate = user.userId?.let { BDDDataSource.getTeamFromUserId(it) }
        teamToUpdate?.let {
            if(!teamToUpdate.contains(newPokemonId)) {
                (teamToUpdate as MutableList<Int>).set(oldPokemon,newPokemonId)
                BDDDataSource.updateTeam(teamToUpdate, user.userId)
            }
        }
    }

    suspend fun insertInTeam(newPokemonId : Int){
        val user = getUser()
        val teamToUpdate = user.userId?.let { BDDDataSource.getTeamFromUserId(it) }
        teamToUpdate?.let {
            if(!teamToUpdate.contains(newPokemonId) && teamToUpdate.size < 6) {
                (teamToUpdate as MutableList<Int>).add(newPokemonId)
                BDDDataSource.updateTeam(teamToUpdate, user.userId)
            }
        }
    }

    suspend fun insertIntoFirestore(user: User){
        val team = user.userId?.let { BDDDataSource.getTeamFromUserId(it) }
        val discoveredPokemon = user.userId?.let { BDDDataSource.getDiscoveredPokemonFromUserId(it) }
        DSFireStore.insertUser(user, team, discoveredPokemon)
    }

    suspend fun getUserFromFirestore(userToken : String) : User?{
        return DSFireStore.getUserByToken(userToken)
    }
}