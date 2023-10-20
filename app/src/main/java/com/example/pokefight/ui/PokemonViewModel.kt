package com.example.pokefight.ui

import androidx.lifecycle.*
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.model.Pokemon
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private var _pokemonLiveData = MutableLiveData<List<Pokemon>>()
    var pokemonLiveData: LiveData<List<Pokemon>> = _pokemonLiveData
    fun fetchPokemons(fromId: Int = 1, toId: Int = fromId + 10) {
        viewModelScope.launch {
            val data = PokemonRepository.fetchPokemons(fromId, toId)
            data.collect {
                _pokemonLiveData.postValue(it)
            }
        }
    }

    fun getPokemonList(fromId: Int = 1, toId: Int = fromId + 10): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
        viewModelScope.launch {
            val data = PokemonRepository.getPokemons(fromId, toId)
            liveData.postValue(data)
        }
        return liveData
    }

    fun getPokemonById(id: Int): LiveData<Pokemon> {
        val liveData = MutableLiveData<Pokemon>()
        viewModelScope.launch {
            val data = PokemonRepository.getPokemonById(id)
            liveData.postValue(data)
        }
        return liveData
    }
}