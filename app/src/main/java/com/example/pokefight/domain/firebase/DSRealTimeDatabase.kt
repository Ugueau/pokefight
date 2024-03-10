package com.example.pokefight.domain.firebase

import android.util.Log
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
        realtime =
            Firebase.database("https://pokefight-36871-default-rtdb.europe-west1.firebasedatabase.app").reference
    }

    suspend fun createNewSwap(creatorToken: String, targetToken: String): Boolean {
        var success: Boolean = true
        realtime.child("swap").child("${creatorToken}_${targetToken}")
            .child("creator_$creatorToken").setValue(-1).addOnFailureListener {
            success = false
        }.await()
        realtime.child("swap").child("${creatorToken}_${targetToken}").child("target_$targetToken")
            .setValue(-1).addOnFailureListener {
            success = false
        }.await()
        realtime.child("swap").child("${creatorToken}_${targetToken}").child("hasValidated")
            .setValue(0).addOnFailureListener {
            success = false
        }.await()

        //Send swap notif to target
        realtime.child("users").child(targetToken).child("swap").child("fromUser")
            .setValue(creatorToken).addOnFailureListener {
            success = false
        }.await()

        return success
    }

    suspend fun setPokemonToSwap(swapName: String, ownerToken: String, pokemonId: Int) {
        val pokemonFrom = if (swapName.split("_")[0] == ownerToken) {
            "creator_$ownerToken"
        } else {
            "target_$ownerToken"
        }

        realtime.child("swap").child(swapName).child(pokemonFrom).setValue(pokemonId).await()
    }

    suspend fun closeSwap(swapName: String): Boolean {
        var success = true
        realtime.child("swap").child(swapName).removeValue().addOnFailureListener {
            success = false
        }.await()
        return success
    }

    suspend fun clearSwapDemand(userToken: String): Boolean {
        var success = true
        realtime.child("users").child(userToken).child("swap").child("fromUser").setValue("")
            .addOnFailureListener {
                success = false
            }
        realtime.child("users").child(userToken).child("swap").child("hasAccepted").setValue("")
            .addOnFailureListener {
                success = false
            }.await()
        return success
    }


    fun setListenerOnSwap(
        swapName: String,
        swaperToken: String,
        callback: (RealTimeDatabaseEvent) -> Unit
    ) {
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //Don't needed
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.key?.let { field ->
                    if (field.contains(swaperToken)) {
                        snapshot.getValue<Int>()?.let { pokemonId ->
                            callback(RealTimeDatabaseEvent.SWAP_POKEMON_CHANGED(pokemonId))
                        }
                    }
                    if (field == "hasValidated") {
                        snapshot.getValue<Int>()?.let { nbOfValidation ->
                            callback(RealTimeDatabaseEvent.SWAP_VALIDATE(nbOfValidation))
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

    suspend fun insertUserInRealTimeDatabase(userToken: String) {
        realtime.child("users").child(userToken).child("swap").child("fromUser").setValue("")
            .addOnFailureListener {
                Log.e("insertUserInRealTimeDatabase", "crashed")
            }.await()
        realtime.child("users").child(userToken).child("swap").child("hasAccepted").setValue("")
            .addOnFailureListener {
                Log.e("insertUserInRealTimeDatabase", "crashed")
            }.await()
        realtime.child("users").child(userToken).child("friend").child("fromUser").setValue("")
            .addOnFailureListener {
                Log.e("insertUserInRealTimeDatabase", "crashed")
            }.await()
        realtime.child("users").child(userToken).child("friend").child("hasAccepted").setValue("")
            .addOnFailureListener {
                Log.e("insertUserInRealTimeDatabase", "crashed")
            }.await()
        realtime.child("users").child(userToken).child("esp32_swap").setValue("")
            .addOnFailureListener {
                Log.e("insertUserInRealTimeDatabase", "crashed")
            }.await()
    }

    fun setNotificationListener(userToken: String, callback: (RealTimeDatabaseEvent) -> Unit) {
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //Don't needed
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key == "swap") {
                    snapshot.child("fromUser").key?.let { field ->
                        snapshot.child("fromUser").getValue<String>()?.let { value ->
                            if (value.isNotEmpty()) {
                                callback(RealTimeDatabaseEvent.SWAP_DEMAND(value))
                            }
                        }
                    }
                    snapshot.child("hasAccepted").key?.let { field ->
                        snapshot.child("hasAccepted").getValue<String>()?.let { value ->
                            if (value == "accepted") {
                                callback(RealTimeDatabaseEvent.SWAP_RESPONSE(true))
                            } else if (value == "denied") {
                                callback(RealTimeDatabaseEvent.SWAP_RESPONSE(false))
                            }
                        }
                    }
                }else if(snapshot.key == "esp32_swap") {
                    snapshot.getValue<String>()?.let {value ->
                        realtime.child("users").child(userToken).child("esp32_swap").setValue("")
                        callback(RealTimeDatabaseEvent.SWAP_CREATE_SWAP(value))
                    }
                }else if(snapshot.key == "friend") {
                    snapshot.child("fromUser").key?.let { field ->
                        snapshot.child("fromUser").getValue<String>()?.let { value ->
                            if (value.isNotEmpty()) {
                                realtime.child("users").child(userToken).child("friend").child("fromUser").setValue("")
                                callback(RealTimeDatabaseEvent.FRIEND_DEMAND(value))
                            }
                        }
                    }
                    snapshot.child("hasAccepted").key?.let { field ->
                        snapshot.child("hasAccepted").getValue<String>()?.let { value ->
                            if (value != "denied" && value != "") {
                                callback(RealTimeDatabaseEvent.FRIEND_RESPONSE(value))
                            } else if (value == "denied") {
                                callback(RealTimeDatabaseEvent.FRIEND_RESPONSE(""))
                            }
                            realtime.child("users").child(userToken).child("friend").child("hasAccepted").setValue("")
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

    suspend fun sendSwapAccept(creatorToken: String) {
        realtime.child("users").child(creatorToken).child("swap").child("hasAccepted")
            .setValue("accepted").await()
    }

    suspend fun sendSwapDeny(creatorToken: String) {
        if (creatorToken.isNotEmpty()) {
            realtime.child("users").child(creatorToken).child("swap").child("hasAccepted")
                .setValue("denied").await()
        }
    }

    suspend fun validateSwap(swapName: String) {
        realtime.child("swap").child(swapName).child("hasValidated").get().addOnSuccessListener {
            val nbOfValidation = it.value as Long
            if (nbOfValidation.toInt() == 1) {
                val creatorToken = swapName.split("_")[0]
                val targetToken = swapName.split("_")[1]
                realtime.child("swap").child(swapName).child("creator_${creatorToken}").get()
                    .addOnSuccessListener { p1 ->
                        val pokemon1 = p1.value as Long
                        realtime.child("swap").child(swapName).child("target_${targetToken}").get()
                            .addOnSuccessListener { p2 ->
                                val pokemon2 = p2.value as Long
                                realtime.child("swap").child(swapName)
                                    .child("creator_${creatorToken}").setValue(pokemon2)
                                realtime.child("swap").child(swapName)
                                    .child("target_${targetToken}").setValue(pokemon1)
                            }
                    }
            }
            realtime.child("swap").child(swapName).child("hasValidated")
                .setValue(nbOfValidation + 1)
        }.await()
    }

    suspend fun askAsAFriend(targetUserToken : String, fromUserToken : String) :Boolean {
        var success = true
        realtime.child("users").child(targetUserToken).child("friend").child("fromUser").setValue(fromUserToken).addOnFailureListener {
            success = false
        }.await()
        return success
    }

    suspend fun sendFriendAccept(askerToken: String, userToken: String) {
        realtime.child("users").child(askerToken).child("friend").child("hasAccepted")
            .setValue(userToken).await()
    }

    suspend fun sendFriendDeny(askerToken: String) {
        if (askerToken.isNotEmpty()) {
            realtime.child("users").child(askerToken).child("friend").child("hasAccepted")
                .setValue("denied").await()
        }
    }

    suspend fun clearUserSpace(userToken: String){
        realtime.child("users").child(userToken).child("friend").child("hasAccepted").setValue("").await()
        realtime.child("users").child(userToken).child("friend").child("fromUser").setValue("").await()
        realtime.child("users").child(userToken).child("esp32_swap").setValue("").await()
        realtime.child("users").child(userToken).child("swap").child("hasAccepted").setValue("").await()
        realtime.child("users").child(userToken).child("swap").child("fromUser").setValue("").await()
    }
}