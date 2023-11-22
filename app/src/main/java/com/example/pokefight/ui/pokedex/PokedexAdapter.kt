package com.example.pokefight.ui.pokedex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.formatId
import com.squareup.picasso.Picasso

class PokedexAdapter(val context : Context, private var pokemonList: List<Pokemon>, private val fragmentManager: FragmentManager)  : RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder>(){
    class PokedexViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val pokemon_name: TextView = view.findViewById(R.id.pokedex_pokemon_name)
        val pokemon_id: TextView = view.findViewById(R.id.pokedex_pokemon_id)
        val pokemon_sprite: ImageView = view.findViewById(R.id.pokedex_pokemon_sprite)
        val pokemon_card : ConstraintLayout = view.findViewById(R.id.pokedex_pokemon)
    }

    fun updatePokemonList(pokemonList: List<Pokemon>){
        this.pokemonList = pokemonList
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
        holder.pokemon_name.text = pokemon.name
        holder.pokemon_id.text = pokemon.formatId(pokemon.id)
        holder.pokemon_card.setOnClickListener {
            val popupPokemonDetail = PopupPokemonDetail()
            popupPokemonDetail.setPokemonToDisplay(pokemon)
            popupPokemonDetail.show(fragmentManager, "popupPokemonDetail")
        }
        val imageUrl = pokemon.sprites.frontDefault
        Picasso.get().load(imageUrl).into(holder.pokemon_sprite)
    }
}