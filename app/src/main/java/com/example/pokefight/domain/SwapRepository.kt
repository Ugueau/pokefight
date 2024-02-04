package com.example.pokefight.domain

import android.util.Log
import com.example.pokefight.domain.firebase.DSFireStore
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

    suspend fun clearSwapDemand() : Boolean{
        val succeeded = DSRealTimeDatabase.clearSwapDemand(UserRepository.getConnectedUser().UserToken)
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

    suspend fun validateSwap() {
        if(currentSwap.isNotEmpty()) {
            DSRealTimeDatabase.validateSwap(currentSwap)
        }
    }

    suspend fun swapPokemons(pokemonId1 : Int, pokemonId2 : Int) : Boolean{
        var success = true

        val teams = UserRepository.getTeam()
        teams.forEach {pokemon ->
            if(pokemon.id == pokemonId1){
                UserRepository.removeFromTeam(pokemonId1)
            }
        }

        UserRepository.addDiscoveredPokemon(pokemonId2)
        UserRepository.removeDiscoveredPokemon(pokemonId1)

        //Only the creator of the swap delete it
        if(currentSwap.split("_")[0] == UserRepository.getConnectedUser().UserToken){
            success = success && DSRealTimeDatabase.closeSwap(currentSwap)
        }

        clearSwapDemand()
        return success
    }
}