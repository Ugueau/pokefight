package com.example.pokefight.ui.swap

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pokefight.R
import com.example.pokefight.model.formatId
import com.example.pokefight.ui.MainViewModel
import com.squareup.picasso.Picasso

class SwapActivity : AppCompatActivity() {
    val mainViewModel by viewModels<MainViewModel>()
    var isSwapFinished = false
    private var pokemonId1 = -1
    private var pokemonId2 = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swap)
    }

    override fun onStart() {
        super.onStart()

        val userName = findViewById<TextView>(R.id.swap_userName2)
        mainViewModel.getSwaperName().observe(this) {
            userName.text = it
        }

        val selectBtn = findViewById<Button>(R.id.swap_selectPokemon)
        selectBtn.setOnClickListener {
            val popup = PopupPokemonSelect()
            popup.show(supportFragmentManager, "popupSelectPokemon")
        }

        val validateBtn = findViewById<Button>(R.id.swap_validate)
        validateBtn.isEnabled = false
        validateBtn.setOnClickListener {
            mainViewModel.validateSwap()
            selectBtn.isEnabled = false
            validateBtn.isEnabled = false
        }

        val pokemon1 = findViewById<ImageView>(R.id.swap_pokemon1)
        val pokemonName1 = findViewById<TextView>(R.id.swap_pokemonName1)
        val pokemonTextId1 = findViewById<TextView>(R.id.swap_pokemonId1)
        mainViewModel.pokemonSelected.observe(this) { pokemonId ->
            mainViewModel.getPokemonById(pokemonId).observe(this) { pokemon ->
                if (pokemon != null) {
                    val imageUrl = pokemon.sprites.backDefault
                    Picasso.get().load(imageUrl).into(pokemon1)
                    pokemonName1.text = pokemon.name
                    pokemonTextId1.text = pokemon.formatId(pokemon.id)
                    pokemonId1 = pokemon.id
                    validateBtn.isEnabled = true
                }
            }
        }

        val pokemon2 = findViewById<ImageView>(R.id.swap_pokemon2)
        val pokemonName2 = findViewById<TextView>(R.id.swap_pokemonName2)
        val pokemonTextId2 = findViewById<TextView>(R.id.swap_pokemonId2)
        mainViewModel.listenOnCurrentSwap() { pokemon ->
            val imageUrl = pokemon.sprites.frontDefault
            Picasso.get().load(imageUrl).into(pokemon2)
            pokemonName2.text = pokemon.name
            pokemonId2 = pokemon.id
            pokemonTextId2.text = pokemon.formatId(pokemon.id)
        }

        mainViewModel.nbOfValidation.observe(this) { it ->
            if (it == 2) {
                if (pokemonId1 != -1 && pokemonId2 != -1) {
                    mainViewModel.swapPokemons(pokemonId1, pokemonId2).observe(this) { success ->
                        if (success) {

                            Toast.makeText(
                                baseContext,
                                "Swap successfully ended",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(baseContext, "Swap failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(baseContext, "Swap failed", Toast.LENGTH_SHORT)
                        .show()
                }
                isSwapFinished = true
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (!isSwapFinished) {
            mainViewModel.endSwapDemand()
            isSwapFinished = true
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        if (!isSwapFinished) {
            mainViewModel.endSwapDemand().observe(this) {
                isSwapFinished = it
            }
        }
        super.onDestroy()
    }
}