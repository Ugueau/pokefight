package com.example.pokefight.ui

import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.BoringLayout
import androidx.lifecycle.*
import com.example.pokefight.MainActivity
import com.example.pokefight.domain.BDD.BDDDataSource
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.User
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _pokemonLiveData = MutableLiveData<List<Pokemon>>()

    private var _userLiveData = MutableLiveData<User>()

    var chosenPokemon = MutableLiveData<Pair<Int, Int>>()

    fun userExistLocal(email: String, password: String): LiveData<User?>{
        //vérifier que l'utilisateur existe dans la BBD local
        val liveData = MutableLiveData<User?>()
        viewModelScope.launch {
            liveData.postValue(UserRepository.userExist(email, password))
        }

        return liveData
    }

    fun connectUser(connectedUser: User){

        viewModelScope.launch {
            UserRepository.connectUser(connectedUser)
        }

    }

    fun getConnectedUserFromCache(): User{
        return UserRepository.getUser()
    }

    fun insertUser(userToInsert: User): LiveData<Boolean>{
        val liveData = MutableLiveData<Boolean>()

        viewModelScope.launch { liveData.postValue(UserRepository.insertUser(userToInsert)) }

        return liveData
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
        chosenPokemon.postValue(Pair(pokemonId,pokemonPosToChange))
    }

    fun getTeam(): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
            viewModelScope.launch {
                val data = UserRepository.getTeam()
                liveData.postValue(data)
        }
        return liveData
    }
}