package com.example.pokefight.domain

import com.example.pokefight.domain.api.DSRetrofit
import com.example.pokefight.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

object PokemonRepository {

    suspend fun getPokemonById(id : Int) : Flow<Response<Pokemon>> = flow {
        emit( DSRetrofit.pokemonService.getPokemonById(id))
    }
}