package com.example.pokefight.domain

import com.example.pokefight.domain.api.DSRetrofit
import com.example.pokefight.domain.cache.DSPokemonCache
import com.example.pokefight.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.Response

object PokemonRepository {

    suspend fun fetchPokemon(fromId: Int, toId: Int): Flow<List<Pokemon>> = flow {
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

        val data = fetchPokemon(fromId, toId)
        data.collect {
            returnedList = it
        }
        return returnedList!!
    }
}