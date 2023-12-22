package com.example.pokefight.domain.BDD

import android.util.Log
import com.example.pokefight.domain.BDD.entity.TeamBDD
import com.example.pokefight.domain.BDD.entity.UserBDD
import com.example.pokefight.model.User

object BDDDataSource {
    suspend fun insertUser(user: User): Boolean{

        lateinit var userCreated: UserBDD

        userCreated = user.toEntity()
        val log = PokefightBDD.getInstance().userDAO().findUserFromEmail(user.Email) == null
        Log.e("bdd", log.toString())
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
            returnUser = User(BDDUser.Email, BDDUser.Password, BDDUser.Nickname, BDDUser.trophy, BDDUser.pokedollar,BDDUser.UserToken,
                BDDUser.idUser!!
            )
        }

        return returnUser
    }

    suspend fun UserExistFromToken(userToken:String): User?{

        var connectedUser = PokefightBDD.getInstance().userDAO().findUserFromToken(userToken)

        if (connectedUser == null){
            return null
        }
        else{
            return User(connectedUser.Email, connectedUser.Password, connectedUser.Nickname, connectedUser.trophy, connectedUser.pokedollar,connectedUser.UserToken, connectedUser.idUser!!)
        }
    }

    suspend fun getDiscoveredPokemonFromUserId(userId : Int): List<Int>{
        val discoveredPokemons = PokefightBDD.getInstance().discoveredPokemonDAO().findDiscoveredPokemonFromUserId(userId)?.discovered
        discoveredPokemons?.let {
            return it
        }
        return emptyList()
    }

    suspend fun getTeamFromUserId(userId : Int): List<Int>{
        val teamPokemons = PokefightBDD.getInstance().teamDAO().findTeamFromUserId(userId)?.teams
        teamPokemons?.let {
            return it
        }
        return emptyList()
    }

    suspend fun updateTeam(newTeam : List<Int>, userId : Int){
        val teamBDD = TeamBDD(newTeam,userId)
        PokefightBDD.getInstance().teamDAO().updateTeam(teamBDD)
    }

    suspend fun insertTeam(newTeam : List<Int>, userId : Int){
        val teamBDD = TeamBDD(newTeam,userId)
        PokefightBDD.getInstance().teamDAO().insertTeam(teamBDD)
    }
}