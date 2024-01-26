package com.example.pokefight.model

sealed class RealTimeDatabaseEvent {
    data class SWAP_DEMAND(val userToken: String) : RealTimeDatabaseEvent()
    data class SWAP_RESPONSE(val response: Boolean) : RealTimeDatabaseEvent()

}