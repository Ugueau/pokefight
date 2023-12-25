package com.example.pokefight.domain.firebase

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

    suspend fun insertUser(user : User, discoveredPokemon : List<Int>?): Boolean {
        val newUser = hashMapOf(
            "email" to user.Email,
            "password" to user.Password,
            "nickname" to user.Nickname,
            "trophy" to user.Trophy,
            "pokedollar" to user.pokedollar
        )

        newUser["team_0"] = 1
        newUser["team_1"] = 4
        newUser["team_2"] = 7

        //TODO ajouter la liste des pokemon découvert

        var succeed = false
         firestore.collection("users").document(user.UserToken).set(newUser).addOnSuccessListener {
             succeed = true
         }
        return succeed
    }

    suspend fun insertInTeam(userToken: String, newTeam : List<Int>){
        val teamSize = getTeamWithUserToken(userToken).size
        if(teamSize < 6){
            val team = hashMapOf<String, Any>()

            newTeam.forEachIndexed { index, teamValue ->
                val fieldName = "team_$index"
                team[fieldName] = teamValue
            }

            firestore.collection("users").document(userToken).update(team)
        }
    }

    suspend fun getUserByToken(userToken : String): User?{
        val userDoc = firestore.collection("users").document(userToken)
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

    suspend fun getTeamWithUserToken(userToken : String): List<Int>{
        val userDoc = firestore.collection("users").document(userToken)
        val team = mutableListOf<Int>()
        val document = userDoc.get().await()
        if (document != null) {
            for(i in 0..5){
                document.getLong("team_$i")?.toInt()?.let {
                    team.add(it)
                }
            }
        }
        return team
    }
}