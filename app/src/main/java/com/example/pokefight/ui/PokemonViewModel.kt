package com.example.pokefight.ui

import androidx.lifecycle.*
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.model.Pokemon
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

class PokemonViewModel: ViewModel() {
    private var _pokemonLiveData = MutableLiveData<Response<Pokemon>>()
    var pokemonLiveData : LiveData<Response<Pokemon>> = _pokemonLiveData
    fun fetchPokemons(){
        viewModelScope.launch {
            val data = PokemonRepository.getPokemonById(1)
            data.collect {
                _pokemonLiveData.postValue(it)
            }
        }
    }

    fun getPokemons() : LiveData<Response<Pokemon>>{
        val liveData = MutableLiveData<Response<Pokemon>>()
        viewModelScope.launch {
            val data = PokemonRepository.getPokemonById(1)
            data.collect{
                liveData.postValue(it)
            }
        }

        return liveData
    }
}