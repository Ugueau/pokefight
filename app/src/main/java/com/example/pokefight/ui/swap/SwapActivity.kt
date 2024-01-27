package com.example.pokefight.ui.swap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.example.pokefight.R
import com.example.pokefight.domain.SwapRepository
import com.example.pokefight.ui.MainViewModel
import com.squareup.picasso.Picasso

class SwapActivity : AppCompatActivity() {
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

        val pokemon2 = findViewById<ImageView>(R.id.swap_pokemon2)
        mainViewModel.listenOnCurrentSwap {pokemon ->
            val imageUrl = pokemon.sprites.frontDefault
            Picasso.get().load(imageUrl).into(pokemon2)
        }

        val validateBtn = findViewById<Button>(R.id.swap_validate)
        validateBtn.setOnClickListener{
            mainViewModel.endSwap().observe(this){
                isSwapFinished = true
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!isSwapFinished){
            mainViewModel.endSwap()
        }
    }
}