package com.example.pokefight.domain.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

object DSFireAuth {
    suspend fun signWithEmailAndPassword(email : String, password : String): String? {
        val auth = Firebase.auth
        try {
            val signing = auth.signInWithEmailAndPassword(email, password).await()
            return signing.user?.uid
        }catch(e: FirebaseAuthInvalidCredentialsException){
            return null
        }
    }

    suspend fun createUserWithEmailAndPassword(email : String, password : String): String? {
        val auth = Firebase.auth
        try {
            val signing = auth.createUserWithEmailAndPassword(email, password).await()
            return signing.user?.uid
        }catch(e: FirebaseAuthInvalidCredentialsException){
            return null
        }
    }
}