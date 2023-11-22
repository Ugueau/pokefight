package com.example.pokefight.domain.cache

import com.example.pokefight.model.Pokemon

object DSPokemonCache {
    private var pokemonList = ArrayList<Pokemon>()

    fun sortCache(){
        pokemonList.sortWith(compareBy { it.id })
    }

    fun addToCache(p : Pokemon): Boolean{
        if (pokemonList.find { it == p } == null){
            pokemonList.add(p)
            return true
        }
        return false
    }
    fun getFromTo(from : Int, to: Int): List<Pokemon>?{
        if(pokemonList.find { it.id == from } != null){
            if (pokemonList.find { it.id == to } != null){
                return pokemonList.subList(from-1,to)
            }
        }
        return null;
    }

    fun isAlreadyLoaded(pokemonId : Int):Boolean{
        return pokemonList.any { it.id == pokemonId }
    }

    fun getPokemon(id :Int): Pokemon?{
        if(pokemonList.find { it.id == id } != null){
            return pokemonList[id]
        }
        return null
    }

    fun getCacheSize():Int{
        return pokemonList.size
    }

    fun all():List<Pokemon>{
        return pokemonList
    }
}