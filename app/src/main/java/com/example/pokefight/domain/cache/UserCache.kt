package com.example.pokefight.domain.cache

import com.example.pokefight.model.User

object UserCache {
    private var user: User? = null

    fun addToCache(newUser : User): Boolean{
        if (user == null){
            user = newUser
            return true
        }
        return false
    }

    fun getUser(): User?{
        if(user != null){
            return user
        }
        return null
    }

    fun updateUserSolde(value: Int) {
        if (user != null) {
            user?.updateSoldeUser(value)
        }
    }

    fun clear(){
        user = null
    }
}