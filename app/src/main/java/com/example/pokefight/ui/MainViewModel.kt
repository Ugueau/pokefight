package com.example.pokefight.ui

import androidx.lifecycle.*
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.User
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _pokemonLiveData = MutableLiveData<List<Pokemon>>()

    var teamUpdated = MutableLiveData<Boolean>()

    fun connectUser(connectedUser: User){

        viewModelScope.launch {
            UserRepository.connectUser(connectedUser)
        }

    }

    fun getConnectedUserFromCache(): User{
        return UserRepository.getConnectedUser()
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

    fun setChosenPokemon(pokemonId : Int, pokemonPosToChange : Int){
        viewModelScope.launch{
            UserRepository.updateTeam(pokemonId,pokemonPosToChange)
            teamUpdated.postValue(true)
        }
    }

    fun addPokemonToTeam(pokemonId : Int){
        viewModelScope.launch{
            UserRepository.insertInTeam(pokemonId)
            teamUpdated.postValue(true)
        }
    }

    fun getTeam(): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
        viewModelScope.launch {
            val data = UserRepository.getTeam()
            liveData.postValue(data)
        }
        return liveData
    }

    fun signIn(email: String, password: String): LiveData<User?>{
        val liveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val uid = UserRepository.signIn(email, password)
            if(uid != null){
                var user = UserRepository.userExist(uid)
                if(user == null){
                    user = UserRepository.fetchUser(uid)
                }
                UserRepository.fetchTeam(uid)
                liveData.postValue(user)
            }else{
                liveData.postValue(null)
            }
        }
        return liveData
    }

    fun createUser(email: String, password: String, nickname : String): LiveData<User?>{
        val liveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val uid = UserRepository.createUser(email, password)
            if(uid != null){
                var user = User(
                    email,
                    password,
                    nickname,
                    0,
                    0,
                    uid,
                    null)
                if (UserRepository.insertUser(user)){
                    liveData.postValue(user)
                }else{
                    liveData.postValue(null)
                }
            }else{
                liveData.postValue(null)
            }
        }
        return liveData
    }

}