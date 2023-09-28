package com.example.pokefight

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokefight.api.ApiManager
import com.example.pokefight.api.IPokemonDataCallback
import com.example.pokefight.databinding.ActivityMainBinding
import com.example.pokefight.model.Pokemon
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), IPokemonDataCallback{

    private lateinit var binding: ActivityMainBinding
    private var pokemonList = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        get20Pokemon()


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun get20Pokemon() {
        for (i in 0..20){
            getPokemonById(this,i)
        }
    }

    private fun getPokemonById(callBack: IPokemonDataCallback, id :Int) {

        val callPokemon: Call<Pokemon>? =
            ApiManager.pokemonService?.getPokemonById(id)
        callPokemon?.enqueue(object : Callback<Pokemon?> {

            override fun onResponse(call: Call<Pokemon?>, response: Response<Pokemon?>) {
                if (response.isSuccessful) {
                    val a: Pokemon? = response.body()
                    a?.let {  }
                    if(a != null){
                        callBack.getPokemonResponseSuccess(a)
                    }
                    else{
                        Log.e("onResponse", "Failed parsing JSON")
                    }
                } else {
                    Log.e("onResponse", "Not successfull : " + response.code())
                    callBack.getPokemonError("Error server response status was : " + response.code())
                }
            }

            override fun onFailure(call: Call<Pokemon?>, t: Throwable) {
                t.localizedMessage?.let { Log.e("onFailure", it) }
                callBack.getPokemonError("Request error : " + t.localizedMessage)
            }


        })
    }

    override fun getPokemonResponseSuccess(pokemon: Pokemon) {
        pokemonList.add(pokemon)
        Log.i("onResponse", "Pokemon "+pokemon.id + ": "+ pokemon.name+" received")
    }

    override fun getPokemonError(message: String) {
        Log.e("onResponse error", message)
    }
}