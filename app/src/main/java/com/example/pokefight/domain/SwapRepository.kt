package com.example.pokefight.domain

import com.example.pokefight.domain.firebase.DSRealTimeDatabase

object SwapRepository {
    private var currentSwap : String = ""

    suspend fun createNewSwap(targetToken : String) : Boolean{
        if (currentSwap.isNotEmpty()){
            return false
        }
        currentSwap = "${UserRepository.getConnectedUser().UserToken}_$targetToken"
        return DSRealTimeDatabase.createNewSwap(UserRepository.getConnectedUser().UserToken,targetToken)
    }

    suspend fun setPokemonToSwap(pokemonId : Int){
        DSRealTimeDatabase.setPokemonToSwap(currentSwap, UserRepository.getConnectedUser().UserToken, pokemonId)
    }

    suspend fun endSwap(){
        DSRealTimeDatabase.endSwap(currentSwap)
        currentSwap = ""
    }

    fun listenOnCurrentSwap(callback : (field :String, value : Int) -> Unit){
        DSRealTimeDatabase.setListenerOnSwap(currentSwap,UserRepository.getConnectedUser().UserToken,callback)
    }
}