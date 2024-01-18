package com.example.pokefight.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.User
import com.example.pokefight.model.stringify
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val teamUpdated = MutableLiveData<Boolean>()
    val userUpdated = MutableLiveData<Boolean>()

    fun connectUser(connectedUser: User){

        viewModelScope.launch {
            UserRepository.connectUser(connectedUser)
        }

    }

    fun updateUserSolde(value: Int){
        viewModelScope.launch {
            UserRepository.updateUserSolde(value)
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

    public val prix_boutique = mutableMapOf<String, Int>(
        "COMMON" to 50,
        "UNCOMMON" to 150,
        "RARE" to 300,
        "POKEBALL" to 100,
        "SUPERBALL" to 250,
        "HYPERBALL" to 500
    )


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

    public var pokemon_boutique = mutableMapOf<String, Int>()

    fun generatePokemonBoutique(){

        val liveData = MutableLiveData<Pokemon?>()

        val iter = true;

        viewModelScope.launch{

        // loop to get the common pokemon
            while(iter){
                val random = java.util.Random()
                val randomNumber = random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

                val data = PokemonRepository.getPokemonById(randomNumber)

                liveData.postValue(data)
            }

        }

    }
}