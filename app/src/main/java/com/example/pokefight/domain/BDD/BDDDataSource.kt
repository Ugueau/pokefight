package com.example.pokefight.domain.BDD

import com.example.pokefight.domain.BDD.entity.DiscoveredPokemonBDD
import com.example.pokefight.domain.BDD.entity.TeamBDD
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

    suspend fun updateUser(user : User, userId : Int){
        //val userBDD = UserBDD(user.Email, user.Nickname,user.Trophy,user.pokedollar,user.UserToken,user.Password)
        PokefightBDD.getInstance().userDAO().forceUpdateUser(user.Email, user.Nickname,user.Trophy,user.pokedollar,user.UserToken,user.Password,userId)
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

        val connectedUser = PokefightBDD.getInstance().userDAO().findUserFromToken(userToken)

        if (connectedUser == null){
            return null
        }
        else{
            return User(connectedUser.Email, connectedUser.Password, connectedUser.Nickname, connectedUser.trophy, connectedUser.pokedollar,connectedUser.UserToken, connectedUser.idUser!!)
        }
    }

    suspend fun updateUsersolde(value: Int, email: String, password: String) {
        PokefightBDD.getInstance().userDAO().updateSoldeUser(value, email, password)
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

    suspend fun updateDiscoveredPokemons(discoveredPokemons : List<Int>, userId : Int){
        val discoveredPokemonBDD = DiscoveredPokemonBDD(discoveredPokemons, userId)
        PokefightBDD.getInstance().discoveredPokemonDAO().updateDiscoveredPokemon(discoveredPokemonBDD)
    }

    suspend fun insertDiscoveredPokemons(discoveredPokemons : List<Int>, userId : Int){
        val discoveredPokemonBDD = DiscoveredPokemonBDD(discoveredPokemons,userId)
        PokefightBDD.getInstance().discoveredPokemonDAO().insertDiscoveredPokemon(discoveredPokemonBDD)
    }
}