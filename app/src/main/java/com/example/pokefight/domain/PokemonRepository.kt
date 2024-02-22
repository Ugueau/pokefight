package com.example.pokefight.domain

import com.example.pokefight.domain.api.DSRetrofit
import com.example.pokefight.domain.cache.DSPokemonCache
import com.example.pokefight.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PokemonRepository {

    const val MAX_ID: Int = 151
    suspend fun fetchPokemons(fromId: Int, toId: Int): Flow<List<Pokemon>> = flow {
        if (DSPokemonCache.getCacheSize() == MAX_ID) {
            emit(DSPokemonCache.all())
        }
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
        emit(fetchedPokemons)
    }

    private suspend fun fetchPokemons(pokemonsToFetch: List<Int>): Flow<List<Pokemon>> = flow {
        val fetchedPokemons = ArrayList<Pokemon>()
        for (i in pokemonsToFetch) {
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
        var returnedList = emptyList<Pokemon>()
        val missingPokemons = ArrayList<Int>()

        for (id in fromId..toId) {
            if (!DSPokemonCache.isAlreadyLoaded(id)) {
                missingPokemons.add(id)
            }
        }

        if (missingPokemons.isEmpty()) {
            DSPokemonCache.getFromTo(fromId, toId)?.let { returnedList = it }
        } else {
            val data = fetchPokemons(missingPokemons)
            data.collect {
                DSPokemonCache.getFromTo(fromId, toId)?.let { returnedList = it }
            }
        }

        return returnedList
    }

    suspend fun getPokemons(pokemonList : List<Int>) : List<Pokemon>{
        DSPokemonCache.sortCache()
        val pokemonToGet = pokemonList.sorted()
        val returnedList = mutableListOf<Pokemon>()
        val missingPokemons = ArrayList<Int>()

        for (id in pokemonToGet) {
            if (!DSPokemonCache.isAlreadyLoaded(id)) {
                missingPokemons.add(id)
            }
        }

        if (missingPokemons.isEmpty()) {
            for (id in pokemonToGet) {
                DSPokemonCache.getPokemon(id)?.let { returnedList.add(it) }
            }

        } else {
            val data = fetchPokemons(missingPokemons)
            data.collect {
                for (id in pokemonToGet) {
                    DSPokemonCache.getPokemon(id)?.let { returnedList.add(it) }
                }
            }
        }

        return returnedList
    }

    suspend fun getPokemonById(id: Int): Pokemon? {
        DSPokemonCache.sortCache()

        var askedPokemon = DSPokemonCache.getPokemon(id)

        if (askedPokemon != null) {
            return askedPokemon
        }

        val data = fetchPokemons(id, id)
        data.collect {
            askedPokemon = it[0]
        }

        return askedPokemon
    }

    fun getLoadedPokemonAmount(): Int {
        return DSPokemonCache.getCacheSize();
    }

}