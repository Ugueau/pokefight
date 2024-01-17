package com.example.pokefight.domain.firebase

import android.util.Log
import com.example.pokefight.ui.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

object DSRealTimeDatabase {
    private lateinit var realtime: DatabaseReference
    fun startRealTimeConnection() {
        //TODO connection checking
        realtime = Firebase.database("https://pokefight-36871-default-rtdb.europe-west1.firebasedatabase.app").reference
    }

    suspend fun createNewSwap(creatorToken : String, targetToken : String) : Boolean{
        var success : Boolean = true
        realtime.child("swap").child("${creatorToken}_${targetToken}").child("creator_$creatorToken").setValue(-1).addOnFailureListener {
            success = false
        }.await()
        realtime.child("swap").child("${creatorToken}_${targetToken}").child("target_$targetToken").setValue(-1).addOnFailureListener {
            success = false
        }.await()
        realtime.child("swap").child("${creatorToken}_${targetToken}").child("isFinished").setValue(false).addOnFailureListener {
            success = false
        }.await()

        return success
    }

    suspend fun setPokemonToSwap(swapName : String, ownerToken : String, pokemonId : Int){
        val pokemonFrom  = if(swapName.split("_")[0] == ownerToken){
            "creator_$ownerToken"
        }else{
            "target_$ownerToken"
        }

        realtime.child("swap").child(swapName).child(pokemonFrom).setValue(pokemonId).await()
    }

    suspend fun endSwap(swapName : String){
        realtime.child("swap").child(swapName).child("isFinished").setValue(true).await()
    }


    fun listenOn(swapName: String, userToken : String, callback: (String,Int) -> Unit){
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //Don't needed
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.key?.let {field ->
                    if(field.contains("creator")){
                        snapshot.getValue<Int>()?.let{value ->
                            callback(field, value)
                        }
                    }else if(field.contains("target")){
                        snapshot.getValue<Int>()?.let{value ->
                            callback(field, value)
                        }
                    }else{
                        callback("error", -1)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //Don't needed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Don't needed
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        realtime.child("swap").child(swapName).addChildEventListener(postListener)
    }
}