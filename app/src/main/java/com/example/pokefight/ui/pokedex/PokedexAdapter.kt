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
import okhttp3.internal.lockAndWaitNanos

class PokedexAdapter(val context : Context, private var pokemonList: List<Pokemon>, private val fragmentManager: FragmentManager)  : RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder>(){
    private var teamMode = false
    private var size : Int = 0
    private lateinit var callbackChoice : (Int) -> Unit
    class PokedexViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val pokemon_name: TextView = view.findViewById(R.id.pokedex_pokemon_name)
        val pokemon_id: TextView = view.findViewById(R.id.pokedex_pokemon_id)
        val pokemon_sprite: ImageView = view.findViewById(R.id.pokedex_pokemon_sprite)
        val pokemon_card : ConstraintLayout = view.findViewById(R.id.pokedex_pokemon)
    }

    fun updatePokemonList(pokemonList: List<Pokemon>){
        this.pokemonList = pokemonList
        size = pokemonList.size
    }

    fun setToTeamChoiceMode(callbackChoice: (Int) -> Unit){
        teamMode = true
        this.callbackChoice = callbackChoice
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokedexViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_pokedex   , parent, false)
        return PokedexViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: PokedexViewHolder, position: Int) {
        val pokemon = pokemonList[position];
        holder.pokemon_name.text = pokemon.name.capitalize()
        holder.pokemon_id.text = pokemon.formatId(pokemon.id)
        if(!teamMode) {
            holder.pokemon_card.setOnClickListener {
                val popupPokemonDetail = PopupPokemonDetail()
                popupPokemonDetail.setPokemonToDisplay(pokemon)
                popupPokemonDetail.show(fragmentManager, "popupPokemonDetail")
            }
        }
        else{
            holder.pokemon_card.setOnClickListener{
                callbackChoice(pokemon.id)
            }
        }
        val imageUrl = pokemon.sprites.frontDefault
        Picasso.get().load(imageUrl).into(holder.pokemon_sprite)
    }
}