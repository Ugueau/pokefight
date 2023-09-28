package com.example.pokefight.api

import com.example.pokefight.model.Pokemon

interface IPokemonDataCallback {
    fun getPokemonResponseSuccess(pokemon: Pokemon)

    fun getPokemonError(message :String)

}