package com.example.pokefight.domain.firebase

import android.util.Log
import com.example.pokefight.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object DSFireStore {
    private lateinit var firestore : FirebaseFirestore
    fun startFireStoreConnection(){
        //TODO connection checking
        firestore = Firebase.firestore
    }

    fun stopFireStoreConnection(){
        firestore.terminate()
        firestore.clearPersistence()
    }

    suspend fun insertUser(user : User, team : List<Int>?, discoveredPokemon : List<Int>?): Boolean {
        val newUser = hashMapOf(
            "email" to user.Email,
            "password" to user.Password,
            "nickname" to user.Nickname,
            "trophy" to user.Trophy,
            "pokedollar" to user.pokedollar
        )

        team?.let { list ->
            if (team.isNotEmpty()) {
                list.forEachIndexed { index, item ->
                    newUser["team_$index"] = item
                }
            }
        }

        //TODO ajouter la liste des pokemon découvert

        var succeed = false
         firestore.collection("users").document(user.UserToken).set(newUser).addOnSuccessListener {
             succeed = true
         }
        return succeed
    }

    suspend fun getUserByToken(userToken : String): User?{
        val userDoc = firestore.collection("cities").document(userToken)
        var user : User? = null
        val document = userDoc.get().await()
        if (document != null) {
            user = User(document.getString("email") ?: "",
                document.getString("password") ?: "",
                document.getString("nickname") ?: "",
                document.getLong("trophy")?.toInt() ?: 0,
                document.getLong("pokedollar")?.toInt() ?: 0,
                userToken,
                null)
        }
        return user
    }
}