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
            if (fromId <= PokemonRepository.MAX_ID) {
                val checkedToId :Int = if(toId > PokemonRepository.MAX_ID){
                    PokemonRepository.MAX_ID
                }else{
                    toId
                }
                val data = PokemonRepository.fetchPokemons(fromId, checkedToId)
                data.collect {
                    _pokemonLiveData.postValue(it)
                }
            }
        }
    }

    fun getPokemonList(fromId: Int = 1, toId: Int = fromId + 10): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
        if (fromId <= PokemonRepository.MAX_ID) {
            val checkedToId :Int = if(toId > PokemonRepository.MAX_ID){
                PokemonRepository.MAX_ID
            }else{
                toId
            }
            viewModelScope.launch {
                val data = PokemonRepository.getPokemons(fromId, checkedToId)
                liveData.postValue(data)
            }
        }
        return liveData
    }

    fun getPokemonById(id: Int): LiveData<Pokemon?> {
        val liveData = MutableLiveData<Pokemon?>()
        if (id <= PokemonRepository.MAX_ID) {
            viewModelScope.launch {
                val data = PokemonRepository.getPokemonById(id)
                liveData.postValue(data)
            }
        }
        return liveData
    }
}