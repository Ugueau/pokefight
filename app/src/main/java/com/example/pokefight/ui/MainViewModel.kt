package com.example.pokefight.ui

import android.content.Intent
import androidx.lifecycle.*
import com.example.pokefight.MainActivity
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.User
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _pokemonLiveData = MutableLiveData<List<Pokemon>>()

    private var _userLiveData = MutableLiveData<User>()



    fun fetchUser(email: String, password: String): Boolean {
        val user = User(email, "Bob le bricolo", 12345678, "0987654321")

        //TODO a refaire lors de la recupération des info sur firebase

        //User par defaut
        if (user.Email == user.getDefaultUser().Email
            && password == user.getDefaultPassword())
        {
           //envoie dans le cache des information du l'utilisateur pour les récupérer dans les autres Activity
            viewModelScope.launch {
                val data = UserRepository.fetchUser(user)
                data.collect {
                    _userLiveData.postValue(it)
                }
            }
            return true
        }
        else
        {
            return false
        }

    }

    fun createUser(user : User){
        viewModelScope.launch {
            val data = UserRepository.createUser(user)
            data.collect {
                _userLiveData.postValue(it)
            }
        }
    }

    fun getConnectedUserFromCache(): User{
        return UserRepository.getUser()
    }

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