package com.example.pokefight.domain.firebase

import com.example.pokefight.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object DSFireStore {
    private lateinit var firestore: FirebaseFirestore
    fun startFireStoreConnection() {
        //TODO connection checking
        firestore = Firebase.firestore
    }

    fun stopFireStoreConnection() {
        firestore.terminate()
        firestore.clearPersistence()
    }

    suspend fun insertUser(user: User): Boolean {
        val newUser = hashMapOf(
            "email" to user.Email,
            "password" to user.Password,
            "nickname" to user.Nickname,
            "trophy" to user.Trophy,
            "pokedollar" to user.pokedollar
        )

        var succeed = false
        firestore.collection("users").document(user.UserToken).set(newUser).addOnSuccessListener {
            firestore.collection("users").document(user.UserToken)
                .update("discovered", listOf(1, 4, 7)).addOnSuccessListener {
                succeed = true
            }
            firestore.collection("users").document(user.UserToken)
                .update("team", listOf(1, 4, 7)).addOnSuccessListener {
                    succeed = true
            }
            firestore.collection("users").document(user.UserToken)
                .update("friends", emptyList<String>()).addOnSuccessListener {
                    succeed = true
                }
        }.await()

        return succeed
    }

    suspend fun insertInTeam(userToken: String, newTeam: List<Int>) {
        firestore.collection("users").document(userToken).update("team",newTeam).await()
    }

    suspend fun insertInDiscoveredPokemon(userToken: String, newDiscoveredList: List<Int>) {
        firestore.collection("users").document(userToken).update("discovered",newDiscoveredList).await()
    }

    suspend fun insertInFriends(userToken: String, newFriendList: List<String>) {
        firestore.collection("users").document(userToken).update("friends",newFriendList).await()
    }

    suspend fun getUserByToken(userToken: String): User? {
        val userDoc = firestore.collection("users").document(userToken)
        var user: User? = null
        val document = userDoc.get().await()
        if (document != null) {
            user = User(
                document.getString("email") ?: "",
                document.getString("password") ?: "",
                document.getString("nickname") ?: "",
                document.getLong("trophy")?.toInt() ?: 0,
                document.getLong("pokedollar")?.toInt() ?: 0,
                userToken,
                null
            )
        }
        return user
    }

    suspend fun getTeamFromUserToken(userToken: String): List<Int> {
        val userDoc = firestore.collection("users").document(userToken)
        val team = mutableListOf<Int>()
        val document = userDoc.get().await()
        if (document != null) {
            (document.get("team") as? List<Int>)?.let {
                team.addAll(it)
            }
        }
        return team
    }

    suspend fun getDiscoveredPokemonFromUserToken(userToken: String): List<Int> {
        val userDoc = firestore.collection("users").document(userToken)
        val discovered = mutableListOf<Int>()
        val document = userDoc.get().await()
        if (document != null) {
            (document.get("discovered") as? List<Int>)?.let {
                discovered.addAll(it)
            }
        }
        return discovered
    }

    suspend fun getFiendsFromUserToken(userToken: String): List<String> {
        val userDoc = firestore.collection("users").document(userToken)
        val friends = mutableListOf<String>()
        val document = userDoc.get().await()
        if (document != null) {
            (document.get("friends") as? List<String>)?.let {
                friends.addAll(it)
            }
        }
        return friends
    }

    suspend fun updateUser(user : User){
        val userUpdate = hashMapOf<String,Any?>(
            "email" to user.Email,
            "password" to user.Password,
            "nickname" to user.Nickname,
            "trophy" to user.Trophy,
            "pokedollar" to user.pokedollar
        )
        firestore.collection("users").document(user.UserToken).update(userUpdate).await()
    }



}