package com.example.pokefight.domain

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

    suspend fun createUser(user : User): Flow<User> = flow{
        //TODO faire l'appel a firebase pour la creation d'utilisateur

        UserCache.addToCache(user)
        emit(user)
    }

    fun getUser(): User {
        var connectedUser = UserCache.getUser()

        if (connectedUser != null) {
            return connectedUser
        }

        return User("", "", 0 , "")
    }
}