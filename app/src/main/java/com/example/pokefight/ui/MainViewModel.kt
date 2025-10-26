package com.example.pokefight.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.example.pokefight.domain.BoutiqueDraws
import com.example.pokefight.domain.BoutiqueRepository
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.domain.SwapRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.domain.api.ConnectionManager
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.RealTimeDatabaseEvent
import com.example.pokefight.model.User
import com.example.pokefight.model.getRarity
import com.example.pokefight.model.stringify
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.log

class MainViewModel : ViewModel() {
    val teamUpdated = MutableLiveData<Boolean>()
    val userUpdated = MutableLiveData<Boolean>()
    val pokemonSelected = MutableLiveData<Int>()
    val nbOfValidation = MutableLiveData<Int>()
    val logout = MutableLiveData<Boolean>()
    val friendsUpdated = MutableLiveData<Boolean>()

    fun connectUser(connectedUser: User) {

        viewModelScope.launch {
            UserRepository.connectUser(connectedUser)
        }

    }

    fun updateUserSolde(value: Int) {
        viewModelScope.launch {
            UserRepository.updateUserSolde(value)
        }
    }

    fun getConnectedUser(): User {
        return UserRepository.getConnectedUser()
    }


    fun getPokemonList(fromId: Int = 1, toId: Int = fromId + 10): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
        if (fromId <= PokemonRepository.MAX_ID) {
            val checkedToId: Int = if (toId > PokemonRepository.MAX_ID) {
                PokemonRepository.MAX_ID
            } else {
                toId
            }
            viewModelScope.launch {
                val data = PokemonRepository.getPokemons(fromId, checkedToId)
                liveData.postValue(data)
            }
        }
        return liveData
    }

    fun getPokemonListByIds(pokemonListIds : List<Int>): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
        val pokemonListIdsFiltered = pokemonListIds.filter { id -> id <= PokemonRepository.MAX_ID }

        viewModelScope.launch {
            val data = PokemonRepository.getPokemons(pokemonListIdsFiltered)
            liveData.postValue(data)
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

    val prix_boutique = mutableMapOf<String, Int>(
        "COMMON" to 50,
        "UNCOMMON" to 150,
        "RARE" to 300,
        "POKEBALL" to 100,
        "SUPERBALL" to 250,
        "HYPERBALL" to 500
    )

    fun setChosenPokemon(pokemonId: Int, pokemonPosToChange: Int) {
        viewModelScope.launch {
            UserRepository.updateTeam(pokemonId, pokemonPosToChange)
            teamUpdated.postValue(true)
        }
    }

    fun addPokemonToTeam(pokemonId: Int) {
        viewModelScope.launch {
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

    fun signIn(email: String, password: String): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val uid = UserRepository.signIn(email, password)
            if (uid != null) {
                val user = UserRepository.fetchUser(uid)
                UserRepository.fetchTeam(uid)
                liveData.postValue(user)
            } else {
                liveData.postValue(null)
            }
        }
        return liveData
    }

    fun createUser(email: String, password: String, nickname: String): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val uid = UserRepository.createUser(email, password)
            if (uid != null) {
                val user = User(
                    email,
                    password,
                    nickname,
                    0,
                    0,
                    uid,
                    null
                )
                if (UserRepository.insertUser(user)) {
                    liveData.postValue(user)
                } else {
                    liveData.postValue(null)
                }
            } else {
                liveData.postValue(null)
            }
        }
        return liveData
    }

    fun addToDiscoveredPokemon(newPokemonId: List<Int>) {
        viewModelScope.launch {
            UserRepository.addDiscoveredPokemon(newPokemonId)
        }
    }

    fun getDiscoveredPokemonsIds(): LiveData<List<Int>> {
        val liveData = MutableLiveData<List<Int>>()
        viewModelScope.launch {
            val data = UserRepository.getDiscoveredPokemon()
            liveData.postValue(data)
        }
        return liveData
    }

    fun getDiscoveredPokemons(): LiveData<List<Pokemon>> {
        val liveData = MutableLiveData<List<Pokemon>>()
        viewModelScope.launch {
            val dp = UserRepository.getDiscoveredPokemon()
            val data = PokemonRepository.getPokemons(dp)
            var log = ""
            data.forEach { pok -> log += pok.stringify() }
            liveData.postValue(data)
        }
        return liveData
    }

    fun updateUser(newUser: User) {
        viewModelScope.launch {
            UserRepository.updateUser(newUser)
            userUpdated.postValue(true)
        }
    }

    fun createNewSwap(targetId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val success = SwapRepository.createNewSwap(targetId)
            liveData.postValue(success)
        }
        return liveData
    }

    fun sendPokemonToSwap(pokemonId: Int) {
        viewModelScope.launch {
            SwapRepository.setPokemonToSwap(pokemonId)
        }
    }

    fun listenOnCurrentSwap(callback: (pokemon: Pokemon) -> Unit) {
        SwapRepository.listenOnCurrentSwap { event ->
            if (event is RealTimeDatabaseEvent.SWAP_POKEMON_CHANGED) {
                viewModelScope.launch {
                    val pokemon = PokemonRepository.getPokemonById(event.pokemonId)
                    pokemon?.let {
                        callback(it)
                    }
                }
            }
            if (event is RealTimeDatabaseEvent.SWAP_VALIDATE) {
                nbOfValidation.postValue(event.nbOfValidation)
            }
        }
    }

    fun setNotificationListener(callback: (RealTimeDatabaseEvent) -> Unit) {
        viewModelScope.launch {
            UserRepository.cleanNotifications()
        }
        UserRepository.setNotificationListener { event ->
            if (event is RealTimeDatabaseEvent.SWAP_DEMAND && event.userToken.isNotEmpty()) {
                SwapRepository.setCurrentSwapName("${event.userToken}_${UserRepository.getConnectedUser().UserToken}")
            } else if (event is RealTimeDatabaseEvent.SWAP_RESPONSE) {
                if (!event.response) {
                    viewModelScope.launch {
                        SwapRepository.clearSwapDemand()
                    }
                }
            }
            callback(event)
        }
    }

    fun getNameOf(userToken: String): LiveData<String> {
        val liveData = MutableLiveData<String>()
        viewModelScope.launch {
            val data = UserRepository.getNameOf(userToken)
            if (data == null) {
                liveData.postValue("")
            }
            data?.let {
                liveData.postValue(it)
            }
        }
        return liveData
    }

    fun sendSwapResponse(response: Boolean) {
        viewModelScope.launch {
            if (response) {
                SwapRepository.sendSwapAccept()
            } else {
                SwapRepository.sendSwapDeny()
            }
        }
    }

    fun endSwapDemand(): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = SwapRepository.clearSwapDemand()
            liveData.postValue(data)
        }
        return liveData
    }

    fun getSwaperName(): LiveData<String> {
        val liveData = MutableLiveData<String>()
        viewModelScope.launch {
            val data = UserRepository.getNameOf(SwapRepository.getSwaperToken())
            if (data == null) {
                liveData.postValue("")
            }
            data?.let {
                liveData.postValue(it)
            }
        }
        return liveData
    }

    fun setPokemonSelectedForSwap(pokemonId: Int) {
        pokemonSelected.postValue(pokemonId)
    }

    fun validateSwap() {
        viewModelScope.launch {
            SwapRepository.validateSwap()
        }
    }

    fun swapPokemons(pokemonId1: Int, pokemonId2: Int): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = SwapRepository.swapPokemons(pokemonId1, pokemonId2)
            liveData.postValue(data)
        }
        return liveData
    }

    // Used by fragment to ask for a swap to activity because popup and swap event are managed in activity
    fun askForASwap(targetUserToken : String) : LiveData<Boolean>
    {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = SwapRepository.askForASwap(targetUserToken)
            liveData.postValue(data)
        }
        return liveData
    }

    fun logout() {
        UserRepository.logout()
        logout.postValue(true)
    }

    fun connectionLost() {
        UserRepository.deconnectUser()
    }

    fun checkNetworkConnection(context: Context): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = ConnectionManager.checkInternetConnection(context)
            liveData.postValue(data)
        }
        return liveData
    }

    fun sendFriendResponse(askerToken: String, response: Boolean) {
        viewModelScope.launch {
            if (response) {
                UserRepository.sendFriendAccept(askerToken)
            } else {
                UserRepository.sendFriendDeny(askerToken)
            }
        }
    }

    fun askAsAFriend(targetUserToken: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val data = UserRepository.askAsAFriend(targetUserToken)
            liveData.postValue(data)
        }
        return liveData
    }

    fun addFriend(friendToken: String) {
        viewModelScope.launch {
            UserRepository.addFriend(friendToken)
            friendsUpdated.postValue(true)
        }
    }

    fun getFriends(): LiveData<List<User>> {
        val liveData = MutableLiveData<List<User>>()
        viewModelScope.launch {
            val data = UserRepository.getFriends()
            liveData.postValue(data)
        }
        return liveData
    }

    fun getDraws() : LiveData<BoutiqueDraws> {
        val liveData = MutableLiveData<BoutiqueDraws>()
        viewModelScope.launch {
            val data = BoutiqueRepository.getLastDraws()
            if (data.savedDate.toLocalDate() == LocalDateTime.now().toLocalDate())
            {
                liveData.postValue(data)
            }
            else
            {
                var pokemonFound = 0
                val newDrawIds = MutableList(3) { 1 }
                while (pokemonFound < 3) {
                    val random = java.util.Random()
                    val randomNumber =
                        random.nextInt(PokemonRepository.MAX_ID) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

                    val pokemon = PokemonRepository.getPokemonById(randomNumber)

                    if (pokemon != null) {
                        if (pokemon.getRarity().name == "COMMON" && newDrawIds[0] == 1) {
                            pokemonFound++
                            newDrawIds[0] = pokemon.id
                        }
                        else if (pokemon.getRarity().name == "UNCOMMON" && newDrawIds[1] == 1) {
                            pokemonFound++
                            newDrawIds[1] = pokemon.id
                        }
                        else if (pokemon.getRarity().name == "RARE" && newDrawIds[2] == 1) {
                            pokemonFound++
                            newDrawIds[2] = pokemon.id
                        }
                    }
                }
                BoutiqueRepository.saveTodayDraws(newDrawIds[0], newDrawIds[1], newDrawIds[2])
                val newDraw = BoutiqueDraws(newDrawIds[0], newDrawIds[1], newDrawIds[2],
                    LocalDateTime.now())
                liveData.postValue(newDraw)
            }
        }
        return liveData
    }

    fun autoAuthUser() : LiveData<User?>
    {
        val liveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val user = UserRepository.isUserStillAuthenticated()
            if (user != null) {
                UserRepository.fetchTeam(user.UserToken)
                liveData.postValue(user)
            } else {
                liveData.postValue(null)
            }
        }
        return liveData
    }


}