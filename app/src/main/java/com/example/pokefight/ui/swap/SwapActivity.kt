package com.example.pokefight.ui.swap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel
import com.squareup.picasso.Picasso

class SwapActivity : AppCompatActivity(){
    val mainViewModel by viewModels<MainViewModel>()
    var isSwapFinished = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swap)
    }

    override fun onStart() {
        super.onStart()

        val userName = findViewById<TextView>(R.id.swap_userName2)
        mainViewModel.getSwaperName().observe(this){
            userName.text = it
        }

        val pokemon1 = findViewById<ImageView>(R.id.swap_pokemon1)
        val pokemonName1 = findViewById<TextView>(R.id.swap_pokemonName1)
        mainViewModel.pokemonSelected.observe(this) {pokemonId ->
            mainViewModel.getPokemonById(pokemonId).observe(this) {pokemon ->
                if(pokemon != null) {
                    val imageUrl = pokemon.sprites.frontDefault
                    Picasso.get().load(imageUrl).into(pokemon1)
                    pokemonName1.text = pokemon.name
                }
            }
        }

        val pokemon2 = findViewById<ImageView>(R.id.swap_pokemon2)
        val pokemonName2 = findViewById<TextView>(R.id.swap_pokemonName2)
        mainViewModel.listenOnCurrentSwap() {pokemon ->
            val imageUrl = pokemon.sprites.frontDefault
            Picasso.get().load(imageUrl).into(pokemon2)
            pokemonName2.text = pokemon.name
        }

        val selectBtn = findViewById<Button>(R.id.swap_selectPokemon)
        selectBtn.setOnClickListener {
            val popup = PopupPokemonSelect()
            popup.show(supportFragmentManager, "popupSelectPokemon")
        }

        val validateBtn = findViewById<Button>(R.id.swap_validate)
        validateBtn.setOnClickListener{
            mainViewModel.validateSwap()
            selectBtn.isEnabled = false
            validateBtn.isEnabled = false
        }

        mainViewModel.nbOfValidation.observe(this){
            if(it == 2){
                mainViewModel.swapPokemons()
                Toast.makeText(baseContext,"Swap successfully ended",Toast.LENGTH_SHORT).show()
                isSwapFinished = true
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!isSwapFinished){
            mainViewModel.endSwapDemand()
        }
    }
}