package com.example.pokefight.domain.BDD

import com.example.pokefight.domain.BDD.entity.UserBDD
import com.example.pokefight.model.User

object BDDDataSource {
    suspend fun insertUser(user: User): Boolean{

        lateinit var userCreated: UserBDD

        userCreated = user.toEntity()

        if (PokefightBDD.getInstance().userDAO().findUserFromEmail(user.Email) == null){
            PokefightBDD.getInstance().userDAO().insertUser(userCreated)
            return true
        }
        else{
            return false
        }

    }

    suspend fun getUserFromEmail(email: String):User?{
        var returnUser: User? = null

        val BDDUser : UserBDD? = PokefightBDD.getInstance().userDAO().findUserFromEmail(email)

        if (BDDUser != null){
            returnUser = User(BDDUser.Email, BDDUser.Password, BDDUser.Nickname, BDDUser.trophy, BDDUser.UserToken)
        }

        return returnUser
    }

    suspend fun UserExistFromEmailAndPassword(email:String, password:String): Boolean{

        return PokefightBDD.getInstance().userDAO().findUserFromEmailAndPassword(email, password) != null
    }
}