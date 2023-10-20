package com.example.pokefight.domain

import com.example.pokefight.domain.api.DSRetrofit
import com.example.pokefight.domain.cache.DSPokemonCache
import com.example.pokefight.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PokemonRepository {

    suspend fun fetchPokemons(fromId: Int, toId: Int): Flow<List<Pokemon>> = flow {
        val fetchedPokemons = ArrayList<Pokemon>()
        for (i in fromId..toId) {
            val pokemon = DSRetrofit.pokemonService.getPokemonById(i)
            if (pokemon.isSuccessful) {
                pokemon.body()?.let {
                    DSPokemonCache.addToCache(it)
                    fetchedPokemons.add(it)
                }
            }
        }
        DSPokemonCache.sortCache()
        emit(fetchedPokemons);
    }

    suspend fun getPokemons(fromId: Int, toId: Int): List<Pokemon> {
        DSPokemonCache.sortCache()

        var returnedList = DSPokemonCache.getFromTo(fromId, toId)

        if (returnedList != null) {
            return returnedList
        }

        val data = fetchPokemons(fromId, toId)
        data.collect {
            returnedList = it
        }
        return returnedList!!
    }

    suspend fun getPokemonById(id: Int): Pokemon {
        DSPokemonCache.sortCache()

        var returnedList = DSPokemonCache.getPokemon(id)

        if (returnedList != null) {
            return returnedList
        }

        val data = fetchPokemons(id,id)
        data.collect {
            returnedList = it[0]
        }
        return returnedList!!
    }
}