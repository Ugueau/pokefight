package com.example.pokefight

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokefight.databinding.ActivityMainBinding
import com.example.pokefight.model.Pokemon
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    val mainViewModel by viewModels<MainViewModel>()
    lateinit var vm : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // valorisation du windowsInsetsController
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // recupération du behavior
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // cacher les barres système
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())



        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //EXAMPLE OF USE

        mainViewModel.pokemonLiveData.observe(this) { pokemonList ->
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
                R.id.navigation_home, R.id.navigation_shop, R.id.navigation_equipe, R.id.navigation_pokedex, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    fun getPokemons(fromId : Int = 1, toId : Int = fromId+10, callback : (List<Pokemon>) -> Unit){
        mainViewModel.getPokemonList(fromId,toId).observe(this){ pokemonList ->
            callback(pokemonList);
        }
    }

    fun getPokemonById(id : Int, callback : (Pokemon) -> Unit){
        mainViewModel.getPokemonById(id).observe(this){ pokemon ->
            callback(pokemon);
        }
    }
}