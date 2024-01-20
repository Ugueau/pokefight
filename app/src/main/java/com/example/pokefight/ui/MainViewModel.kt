package com.example.pokefight.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.domain.SwapRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.RealTimeDatabaseEvent
import com.example.pokefight.model.User
import com.example.pokefight.model.stringify
import com.example.pokefight.ui.swap.SwapFragment
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val teamUpdated = MutableLiveData<Boolean>()
    val userUpdated = MutableLiveData<Boolean>()

    fun connectUser(connectedUser: User){

        viewModelScope.launch {
            UserRepository.connectUser(connectedUser)
        }

    }

    fun getConnectedUser(): User{
        return UserRepository.getConnectedUser()
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
                val user = UserRepository.fetchUser(uid)
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
                val user = User(
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

    fun addToDiscoveredPokemon(pokemonId: Int){
        viewModelScope.launch {
            UserRepository.addDiscoveredPokemon(pokemonId)
        }
    }

    fun getDiscoveredPokemonsIds() : LiveData<List<Int>>{
        val liveData = MutableLiveData<List<Int>>()
        viewModelScope.launch {
            val data = UserRepository.getDiscoveredPokemon()
            liveData.postValue(data)
        }
        return liveData
    }

    fun getDiscoveredPokemons() : LiveData<List<Pokemon>>{
        val liveData = MutableLiveData<List<Pokemon>>()
        viewModelScope.launch {
            val dp = UserRepository.getDiscoveredPokemon()
            Log.e("dpi",dp.toString())
            val data = PokemonRepository.getPokemons(dp)
            var log = ""
            data.forEach{pok -> log += pok.stringify()}
            Log.e("dp2", log)
            liveData.postValue(data)
        }
        return liveData
    }

    fun updateUser(newUser : User){
        viewModelScope.launch {
            UserRepository.updateUser(newUser)
            userUpdated.postValue(true)
        }
    }

    fun createNewSwap(targetId : String) : LiveData<Boolean>{
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val success = SwapRepository.createNewSwap(targetId)
            if(success) {
                listenOnCurrentSwap { field, value ->
                    //Here put the callback to call when a pokemonId changed in the current swap
                    Log.e("RealTimeListener", "${field} : ${value.toString()}")
                }
            }
            liveData.postValue(success)
        }
        return liveData
    }

    fun sendPokemonToSwap(pokemonId : Int){
        viewModelScope.launch {
            SwapRepository.setPokemonToSwap(pokemonId)
        }
    }

    private fun listenOnCurrentSwap(callback : (String, Int) -> Unit){
        SwapRepository.listenOnCurrentSwap(callback)
    }

    fun setNotificationListener(callback: (RealTimeDatabaseEvent) -> Unit){
        viewModelScope.launch {
            UserRepository.setNotificationListener { event ->
                callback(event)
            }
        }
    }
}