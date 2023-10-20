package com.example.pokefight

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokefight.databinding.ActivityMainBinding
import com.example.pokefight.ui.PokemonViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    val pokemonViewModel by viewModels<PokemonViewModel>()
    lateinit var vm : PokemonViewModel

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

        

        vm = ViewModelProvider(this).get(PokemonViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemonViewModel.pokemonLiveData.observe(this) { response ->
            if (response?.isSuccessful == true) {
                response.body()?.let {
                    Log.e("Pokemon returned in main", it.name)
                }
            }
        }
        pokemonViewModel.fetchPokemons()

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
}