package com.example.pokefight.domain.api

import com.example.pokefight.model.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IPokemonService {
    @GET("pokemon/{id}/") // Use {id} as a path parameter
    suspend fun getPokemonById(@Path("id") id: Int): Response<Pokemon>
}