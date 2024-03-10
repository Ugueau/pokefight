package com.example.pokefight.model

sealed class RealTimeDatabaseEvent {
    data class SWAP_DEMAND(val userToken: String) : RealTimeDatabaseEvent()
    data class SWAP_RESPONSE(val response: Boolean) : RealTimeDatabaseEvent()
    data class SWAP_POKEMON_CHANGED(val pokemonId: Int) : RealTimeDatabaseEvent()
    data class SWAP_VALIDATE(val nbOfValidation : Int) : RealTimeDatabaseEvent()
    data class SWAP_CREATE_SWAP(val targetToken : String) : RealTimeDatabaseEvent()
    data class FRIEND_DEMAND(val userToken: String) : RealTimeDatabaseEvent()
    data class FRIEND_RESPONSE(val userToken: String) : RealTimeDatabaseEvent()

}