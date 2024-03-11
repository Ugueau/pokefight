package com.example.pokefight

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.ui.MainViewModel
import com.squareup.picasso.Picasso

class DiscoveredPokemonActivity : AppCompatActivity() {

    val mainViewModel by viewModels<MainViewModel>()
    lateinit var vm: MainViewModel

    private var pokemonPokeball: Int = 0
    private var pokemonSuperball: Int = 0
    private var pokemonHyperball: Int = 0

    private lateinit var idPurchase: String

    private var counter = 0

    private lateinit var pokemonImage: ImageView
    private lateinit var pokemonName: TextView
    private lateinit var nextPokemon: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discovered_pokemon)

        // valorisation du windowsInsetsController
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // recupération du behavior
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // cacher les barres système
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        vm = ViewModelProvider(this)[MainViewModel::class.java]

        pokemonImage = this.findViewById(R.id.newPokemon)
        pokemonName = this.findViewById(R.id.newPokemonName)
        nextPokemon = this.findViewById(R.id.nextPokemon)

        nextPokemon.setOnClickListener {
            nextPokemonClicked()
        }

        val intent = intent
        pokemonPokeball = intent.getIntExtra("POKEMON_POKEBALL", 0)
        pokemonSuperball = intent.getIntExtra("POKEMON_SUPERBALL", 0)
        pokemonHyperball = intent.getIntExtra("POKEMON_HYPERBALL", 0)

        idPurchase = intent.getStringExtra("PURCHASE") ?: ""

        if (idPurchase != "") {
            when (idPurchase) {
                "COMMON" -> {
                    if (pokemonPokeball != 0) {

                        vm.getPokemonById(pokemonPokeball).observe(this) {
                            val imageUrlRare = it!!.sprites.frontDefault
                            Picasso.get().load(imageUrlRare).into(pokemonImage)
                            pokemonName.text = it.name
                            counter = 0
                        }
                    }
                }

                "UNCOMMON" -> {
                    if (pokemonPokeball != 0) {
                        vm.getPokemonById(pokemonPokeball).observe(this) {
                            val imageUrlRare = it!!.sprites.frontDefault
                            Picasso.get().load(imageUrlRare).into(pokemonImage)
                            pokemonName.text = it.name
                            counter = 0
                        }
                    }
                }

                "RARE" -> {
                    if (pokemonPokeball != 0) {
                        vm.getPokemonById(pokemonPokeball).observe(this) {
                            val imageUrlRare = it!!.sprites.frontDefault
                            Picasso.get().load(imageUrlRare).into(pokemonImage)
                            pokemonName.text = it.name
                            counter = 0
                        }
                    }
                }

                "POKEBALL" -> {
                    if (pokemonPokeball != 0) {
                        vm.getPokemonById(pokemonPokeball).observe(this) {
                            val imageUrlRare = it!!.sprites.frontDefault
                            Picasso.get().load(imageUrlRare).into(pokemonImage)
                            pokemonName.text = it.name
                            counter = 0
                        }
                    }
                }

                "SUPERBALL" -> {
                    if (pokemonSuperball != 0) {
                        vm.getPokemonById(pokemonSuperball).observe(this) {
                            val imageUrlRare = it!!.sprites.frontDefault
                            Picasso.get().load(imageUrlRare).into(pokemonImage)
                            pokemonName.text = it.name
                            counter = 1
                        }
                    }

                }

                "HYPERBALL" -> {
                    if (pokemonHyperball != 0) {
                        vm.getPokemonById(pokemonHyperball).observe(this) {
                            val imageUrlRare = it!!.sprites.frontDefault
                            Picasso.get().load(imageUrlRare).into(pokemonImage)
                            pokemonName.text = it.name
                            counter = 2
                        }
                    }
                }
            }
        }
    }

    private fun nextPokemonClicked() {
        if (counter == 2) {
            if (pokemonSuperball != 0) {
                vm.getPokemonById(pokemonSuperball).observe(this) {
                    val imageUrlRare = it!!.sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(pokemonImage)
                    pokemonName.text = it.name
                    counter = 1
                }
            }
        }
        if (counter == 1) {
            if (pokemonPokeball != 0) {
                vm.getPokemonById(pokemonPokeball).observe(this) {
                    val imageUrlRare = it!!.sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(pokemonImage)
                    pokemonName.text = it.name
                    counter = 0
                }
            }
        }
        if (counter == 0) {
            this.finish()
        }
    }
}