package com.example.pokefight.domain

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.pokefight.domain.BDD.BDDDataSource
import com.example.pokefight.domain.BDD.entity.UserBDD
import com.example.pokefight.domain.api.DSRetrofit
import com.example.pokefight.domain.cache.DSPokemonCache
import com.example.pokefight.domain.cache.UserCache
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {

    suspend fun fetchUser(connectedUser: User): Flow<User> = flow {
        //TODO a faire lorsque l'api firebase sera faite



        //ajout dans le cache du user connecté
        UserCache.addToCache(connectedUser)
        emit(connectedUser)
    }

    suspend fun userExist(email: String, password: String): User?{
        return BDDDataSource.UserExistFromEmailAndPassword(email, password)
    }

    suspend fun insertUser(user : User): Boolean{
        var toReturn: Boolean = false

        //TODO faire l'appel a firebase pour la creation d'utilisateur

        //Todo faire l'appel a insert de room
        toReturn = BDDDataSource.insertUser(user)
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
        (teamToUpdate as MutableList<Int>).set(oldPokemon,newPokemonId)
        BDDDataSource.updateTeam(teamToUpdate, user.userId)
    }
}