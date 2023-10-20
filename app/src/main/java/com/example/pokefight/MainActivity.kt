package com.example.pokefight

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokefight.databinding.ActivityMainBinding
import com.example.pokefight.model.Pokemon
import com.example.pokefight.ui.PokemonViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    val pokemonViewModel by viewModels<PokemonViewModel>()
    lateinit var vm : PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this).get(PokemonViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //EXAMPLE OF USE

        pokemonViewModel.pokemonLiveData.observe(this) {pokemonList ->
            var nameList = String()
            pokemonList.forEach{
                nameList += "${it.name} ; "
            }
            Log.e("From cache",nameList)
        }

        getPokemons(1,10){pokemonList ->
            var nameList = String()
            pokemonList.forEach{
                nameList += "${it.name} ; "
            }
            Log.e("From cache",nameList)
        }
        // ------

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

    fun getPokemons(fromId : Int = 1, toId : Int = fromId+10, callback : (List<Pokemon>) -> Unit){
        pokemonViewModel.getPokemonList(1,15).observe(this){pokemonList ->
            callback(pokemonList);
        }
    }
}