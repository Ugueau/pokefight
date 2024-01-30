package com.example.pokefight.domain.firebase

import android.util.Log
import com.example.pokefight.domain.SwapRepository
import com.example.pokefight.model.RealTimeDatabaseEvent
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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

        //Send swap notif to target
        Log.e("tes sur ?", "oui")
        realtime.child("users").child(targetToken).child("swap").child("fromUser").setValue(creatorToken).addOnFailureListener {
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

    suspend fun closeSwap(swapName : String, userToken: String) : Boolean{
        var success = true
        realtime.child("swap").child(swapName).removeValue().addOnFailureListener{
            success = false
        }.await()
        realtime.child("users").child(userToken).child("swap").child("fromUser").setValue("").addOnFailureListener {
            success = false
        }
        realtime.child("users").child(userToken).child("swap").child("hasAccepted").setValue("").addOnFailureListener {
            success = false
        }.await()
        return success
    }


    fun setListenerOnSwap(swapName: String, swaperToken : String, callback: (RealTimeDatabaseEvent) -> Unit){
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //Don't needed
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.key?.let {field ->
                    if(field.contains(swaperToken)){
                        snapshot.getValue<Int>()?.let{pokemonId ->
                            callback(RealTimeDatabaseEvent.SWAP_POKEMON_CHANGED(pokemonId))
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

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

    suspend fun insertUserInRealTimeDatabase(userToken : String){
        realtime.child("users").child(userToken).child("swap").child("fromUser").setValue("").addOnFailureListener {
            Log.e("non", "crashed")
        }.await()
        realtime.child("users").child(userToken).child("swap").child("hasAccepted").setValue("").addOnFailureListener {
            Log.e("non", "crashed")
        }.await()
        realtime.child("users").child(userToken).child("friend").setValue("").addOnFailureListener {
            Log.e("non", "crashed")
        }.await()
    }

    fun setNotificationListener(userToken : String, callback: (RealTimeDatabaseEvent) -> Unit){
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //Don't needed
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.child("fromUser").key?.let { field ->
                    snapshot.child("fromUser").getValue<String>()?.let { value ->
                        if(value.isNotEmpty()) {
                            Log.e("fromUser", value)
                            callback(RealTimeDatabaseEvent.SWAP_DEMAND(value))
                        }
                    }
                }
                snapshot.child("hasAccepted").key?.let { field ->
                    snapshot.child("hasAccepted").getValue<String>()?.let{value ->
                        if(value == "accepted"){
                            Log.e("hasaccepted", value)
                            callback(RealTimeDatabaseEvent.SWAP_RESPONSE(true))
                        }else if(value == "denied"){
                            callback(RealTimeDatabaseEvent.SWAP_RESPONSE(false))
                            Log.e("hasaccepted", value)
                        }
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
        realtime.child("users").child(userToken).addChildEventListener(postListener)
    }

    suspend fun sendSwapAccept(creatorToken : String) {
        realtime.child("users").child(creatorToken).child("swap").child("hasAccepted").setValue("accepted").await()
    }

    suspend fun sendSwapDeny(creatorToken : String){
        if(creatorToken.isNotEmpty()){
            realtime.child("users").child(creatorToken).child("swap").child("hasAccepted").setValue("denied").await()
        }
    }
}