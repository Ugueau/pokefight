package com.example.pokefight.domain

import com.example.pokefight.domain.BDD.BDDDataSource
import com.example.pokefight.domain.cache.UserCache
import com.example.pokefight.domain.firebase.DSFireAuth
import com.example.pokefight.domain.firebase.DSFireStore
import com.example.pokefight.domain.firebase.DSRealTimeDatabase
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.RealTimeDatabaseEvent
import com.example.pokefight.model.User

object UserRepository {

    suspend fun userExist(userToken: String): User? {
        return BDDDataSource.UserExistFromToken(userToken)
    }

    suspend fun insertUser(user: User): Boolean {
        var toReturn: Boolean = false
        toReturn = BDDDataSource.insertUser(user)
        if (toReturn) {
            BDDDataSource.UserExistFromToken(user.UserToken)?.let {
                insertIntoFirestore(it)
                DSRealTimeDatabase.insertUserInRealTimeDatabase(user.UserToken)
            }
        }
        return toReturn
    }

    suspend fun updateUser(user : User){
        user.userId?.let {
            BDDDataSource.updateUser(user, it)
            DSFireStore.updateUser(user)
            deconnectUser()
            connectUser(user)
        }
    }

    fun getConnectedUser(): User {
        var connectedUser = UserCache.getUser()

        if (connectedUser != null) {
            return connectedUser
        }

        return User("", "", "", 0, 0, "", -1)
    }

    suspend fun connectUser(connectedUser: User) {
        UserCache.addToCache(connectedUser)
    }

    suspend fun updateUserSolde(value: Int) {

        UserCache.updateUserSolde(value)

        val user = this.getConnectedUser()

        BDDDataSource.updateUsersolde(user.pokedollar, user.Email, user.Password)

        DSFireStore.updateUser(user)
    }

    fun deconnectUser(){
        UserCache.clear()
    }

    suspend fun getDiscoveredPokemon(): List<Int> {
        val userId = UserCache.getUser()?.userId
        userId?.let {
            return BDDDataSource.getDiscoveredPokemonFromUserId(userId)
        }
        return emptyList()
    }

    suspend fun getTeam(): List<Pokemon> {
        val userId = UserCache.getUser()?.userId
        userId?.let {
            val pokemonTeam = mutableListOf<Pokemon>()
            BDDDataSource.getTeamFromUserId(userId).forEach { id ->
                PokemonRepository.getPokemonById(id)?.let { pokemon ->
                    pokemonTeam.add(pokemon)
                }
            }
            return pokemonTeam
        }
        return emptyList()
    }

    suspend fun updateTeam(newPokemonId: Int, oldPokemonIndex: Int) {
        val user = getConnectedUser()
        val teamToUpdate = user.userId?.let { BDDDataSource.getTeamFromUserId(it) }
        teamToUpdate?.let {
            if (!teamToUpdate.contains(newPokemonId) && teamToUpdate.size <= 6) {
                (teamToUpdate as MutableList<Int>).set(oldPokemonIndex, newPokemonId)
                BDDDataSource.updateTeam(teamToUpdate, user.userId)
                DSFireStore.insertInTeam(user.UserToken, teamToUpdate)
            }
        }
    }

    suspend fun insertInTeam(newPokemonId: Int) {
        val user = getConnectedUser()
        if (user.userId != null) {
            var teamToUpdate = BDDDataSource.getTeamFromUserId(user.userId)
            teamToUpdate = teamToUpdate.toMutableList()
            if (!teamToUpdate.contains(newPokemonId) && teamToUpdate.size < 6) {
                teamToUpdate.add(newPokemonId)
                BDDDataSource.updateTeam(teamToUpdate, user.userId)
                DSFireStore.insertInTeam(user.UserToken, teamToUpdate)
            }
        }
    }

    suspend fun insertIntoFirestore(user: User) {
        DSFireStore.insertUser(user)
    }

    suspend fun fetchTeam(userToken: String): List<Int>? {
        val user = BDDDataSource.UserExistFromToken(userToken)
        if (user?.userId != null) {
            val discoveredPokemon = DSFireStore.getDiscoveredPokemonFromUserToken(user.UserToken)
            val oldTeam = BDDDataSource.getTeamFromUserId(user.userId)
            val newTeam = DSFireStore.getTeamFromUserToken(userToken)
            if (newTeam.size < 6) {
                if (oldTeam.isEmpty()) {
                    BDDDataSource.insertTeam(newTeam, user.userId)
                    BDDDataSource.insertDiscoveredPokemons(discoveredPokemon, user.userId)
                } else {
                    BDDDataSource.updateTeam(newTeam, user.userId)
                    BDDDataSource.updateDiscoveredPokemons(discoveredPokemon, user.userId)
                }
                return newTeam
            }
        }
        return null
    }

    suspend fun fetchUser(userToken: String): User? {
        val user = BDDDataSource.UserExistFromToken(userToken)
        val userData = DSFireStore.getUserByToken(userToken)
        if (user?.userId != null) {
            if (userData != null) {
                BDDDataSource.updateUser(userData, user.userId)
                BDDDataSource.UserExistFromToken(userToken)?.let {
                    return it
                }
            }
        } else {
            if (userData != null && BDDDataSource.insertUser(userData)) {
                BDDDataSource.UserExistFromToken(userToken)?.let {
                    return it
                }
            }
        }
        return null
    }

    suspend fun signIn(email: String, password: String): String? {
        return DSFireAuth.signWithEmailAndPassword(email, password)
    }

    suspend fun createUser(email: String, password: String): String? {
        return DSFireAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun addDiscoveredPokemon(newPokemonId: List<Int>) {
        val user = getConnectedUser()
        if (user.userId != null) {
            var alreadyDiscovered = BDDDataSource.getDiscoveredPokemonFromUserId(user.userId)
            alreadyDiscovered = alreadyDiscovered.toMutableList()
            newPokemonId.forEach { pokemonId ->
                if (!alreadyDiscovered.contains(pokemonId)) {
                    alreadyDiscovered.add(pokemonId)
                }
            }
            BDDDataSource.updateDiscoveredPokemons(alreadyDiscovered, user.userId)
            DSFireStore.insertInDiscoveredPokemon(user.UserToken, alreadyDiscovered)
        }
    }

    suspend fun removeDiscoveredPokemon(pokemonId : Int) {
        val user = getConnectedUser()
        if (user.userId != null) {
            var alreadyDiscovered = BDDDataSource.getDiscoveredPokemonFromUserId(user.userId)
            alreadyDiscovered = alreadyDiscovered.toMutableList()
            alreadyDiscovered.remove(pokemonId)
            BDDDataSource.updateDiscoveredPokemons(alreadyDiscovered, user.userId)
            DSFireStore.insertInDiscoveredPokemon(user.UserToken, alreadyDiscovered)
        }
    }

    fun setNotificationListener(callback : (RealTimeDatabaseEvent) -> Unit){
        DSRealTimeDatabase.setNotificationListener(UserRepository.getConnectedUser().UserToken, callback)
    }

    suspend fun getNameOf(userToken : String):String? {
        return DSFireStore.getUserByToken(userToken)?.Nickname
    }

    suspend fun removeFromTeam(pokemonToRemove : Int) {
        val user = getConnectedUser()
        val teamToUpdate = user.userId?.let { BDDDataSource.getTeamFromUserId(it) } as MutableList<Int>
        teamToUpdate.let {
            if (teamToUpdate.contains(pokemonToRemove)) {
                teamToUpdate.remove(pokemonToRemove)
                BDDDataSource.updateTeam(teamToUpdate, user.userId)
                DSFireStore.insertInTeam(user.UserToken, teamToUpdate)
            }
        }
    }

    fun logout() {
        deconnectUser()
        DSFireAuth.logout()
    }

    suspend fun sendFriendAccept(askerToken :String) {
        DSRealTimeDatabase.sendFriendAccept(askerToken, getConnectedUser().UserToken)
    }

    suspend fun sendFriendDeny(askerToken :String) {
        DSRealTimeDatabase.sendFriendDeny(askerToken)
    }

    suspend fun askAsAFriend(targetUserToken : String) : Boolean {
        return DSRealTimeDatabase.askAsAFriend(targetUserToken, getConnectedUser().UserToken)
    }

    suspend fun cleanNotifications(){
        DSRealTimeDatabase.clearUserSpace(getConnectedUser().UserToken)
    }

    suspend fun addFriend(friendToken : String){
        val user = getConnectedUser()
        if (user.userId != null) {
            val friends = DSFireStore.getFiendsFromUserToken(user.UserToken)
            val newFriendList = friends.toMutableList()
            if(!friends.contains(friendToken)){
                newFriendList.add(friendToken)
            }
            DSFireStore.insertInFriends(user.UserToken, newFriendList)
        }
    }

    suspend fun getFriends() : List<User> {
        val friendTokenList = DSFireStore.getFiendsFromUserToken(getConnectedUser().UserToken)
        val friends = mutableListOf<User>()
        friendTokenList.forEach {friendToken ->
            DSFireStore.getUserByToken(friendToken)?.let {
                friends.add(it)
            }
        }
        return friends
    }
}