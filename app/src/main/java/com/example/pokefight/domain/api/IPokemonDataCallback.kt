package com.example.pokefight.domain.api

import com.example.pokefight.model.Pokemon

interface IPokemonDataCallback {
    fun getPokemonResponseSuccess(pokemon: Pokemon)

    fun getPokemonError(message :String)

}