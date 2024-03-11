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

    suspend fun clearSwapDemand() : Boolean{
        //Only the creator of the swap delete it
        if(currentSwap.isNotEmpty() && currentSwap.split("_")[0] == UserRepository.getConnectedUser().UserToken){
            DSRealTimeDatabase.closeSwap(currentSwap)
        }
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
        if(currentSwap.isNotEmpty()) {
            val creatorToken = currentSwap.split("_")[0]
            DSRealTimeDatabase.sendSwapAccept(creatorToken)
        }
    }

    suspend fun sendSwapDeny(){
        if(currentSwap.isNotEmpty()) {
            //Don't remove this !
            val tmp = currentSwap
            val creatorToken = tmp.split("_")[0]
            DSRealTimeDatabase.sendSwapDeny(creatorToken)
            val targetToken = tmp.split("_")[1]
            DSRealTimeDatabase.sendSwapDeny(targetToken)
        }
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
        val newDP = listOf<Int>(pokemonId2)
        UserRepository.addDiscoveredPokemon(newDP)
        UserRepository.removeDiscoveredPokemon(pokemonId1)


        clearSwapDemand()
        return success
    }
}