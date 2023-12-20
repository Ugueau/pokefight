package com.example.pokefight.domain.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

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
}