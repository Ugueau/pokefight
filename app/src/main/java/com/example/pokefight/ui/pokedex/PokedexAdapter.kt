package com.example.pokefight.ui.pokedex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.model.Pokemon

class PokedexAdapter(val context : Context, private val pokemonList: List<Pokemon>)  : RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder>(){
    class PokedexViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.pokemon_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokedexViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_pokedex   , parent, false)
        return PokedexViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return pokemonList.size;
    }

    override fun onBindViewHolder(holder: PokedexViewHolder, position: Int) {
        val pokemon = pokemonList[position];
        holder.textView.text = pokemon.name
    }
}