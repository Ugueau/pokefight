package com.example.pokefight.domain

import com.example.pokefight.domain.firebase.DSRealTimeDatabase
import com.example.pokefight.model.RealTimeDatabaseEvent

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

    suspend fun endSwap() : Boolean{
        val succeeded = DSRealTimeDatabase.closeSwap(currentSwap, UserRepository.getConnectedUser().UserToken)
        currentSwap = ""
        return succeeded
    }

    fun listenOnCurrentSwap(callback : (RealTimeDatabaseEvent) -> Unit){
        DSRealTimeDatabase.setListenerOnSwap(currentSwap, getSwaperToken(),callback)
    }

    fun setCurrentSwapName(newCurrentSwap : String){
        currentSwap = newCurrentSwap
    }
    suspend fun sendSwapAccept(){
        val creatorToken = currentSwap.split("_")[0]
        DSRealTimeDatabase.sendSwapAccept(creatorToken)
        DSRealTimeDatabase.sendSwapAccept(UserRepository.getConnectedUser().UserToken)
    }

    suspend fun sendSwapDeny(){
        val creatorToken = currentSwap.split("_")[0]
        DSRealTimeDatabase.sendSwapDeny(creatorToken)
    }

    fun getSwaperToken() : String{
        if(currentSwap.isNotEmpty()){
            if(UserRepository.getConnectedUser().UserToken == currentSwap.split("_")[0]){
                return currentSwap.split("_")[1]
            }else{
                return currentSwap.split("_")[0]
            }
        }else{
            return ""
        }
    }
}