package com.example.pokefight.services

import com.example.pokefight.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon/")
    fun getPokemonById(@Query("id") id:Int): Call<Pokemon?>?

    @GET("pokemon/1/")
    fun getBulbasaur(): Call<Pokemon?>?

    /*@GET("?dataset=accidents-corporels-de-la-circulation-en-france&sort=-nbimplique")
    fun getAccidents(
        @Query("start") start: Int,
        @Query("rows") rows: Int,
        @Query("geofilter.distance") geofilterDistance: String?
    ): Call<Accidents?>?*/
}