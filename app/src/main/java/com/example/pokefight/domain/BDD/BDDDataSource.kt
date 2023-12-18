package com.example.pokefight.domain.BDD

import com.example.pokefight.domain.BDD.entity.TeamBDD
import com.example.pokefight.domain.BDD.entity.UserBDD
import com.example.pokefight.model.User

object BDDDataSource {
    suspend fun insertUser(user: User): Boolean{

        lateinit var userCreated: UserBDD

        userCreated = user.toEntity()
        val defaultPokemonIds = listOf<Int>(1,4,7)
        val defaultTeam = TeamBDD(defaultPokemonIds,user.userId)

        if (PokefightBDD.getInstance().userDAO().findUserFromEmail(user.Email) == null){
            PokefightBDD.getInstance().userDAO().insertUser(userCreated)
            PokefightBDD.getInstance().teamDAO().insertTeam(defaultTeam)
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

    suspend fun UserExistFromEmailAndPassword(email:String, password:String): User?{

        var connectedUser = PokefightBDD.getInstance().userDAO().findUserFromEmailAndPassword(email, password)

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

    suspend fun updateTeam(newTeams : List<Int>, userId : Int){
        val teamBDD = TeamBDD(teams = newTeams,userId)
        PokefightBDD.getInstance().teamDAO().updateTeam(teamBDD)
    }
}