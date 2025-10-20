package com.example.pokefight.ui.pokedex

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.formatId
import com.example.pokefight.tools.ColorHelper
import com.squareup.picasso.Picasso
import timber.log.Timber

class PokedexAdapter(val context : Context, private var pokemonList: List<Pokemon>, private val fragmentManager: FragmentManager)  : RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder>(){
    private var teamMode = false
    private var size : Int = 0
    private var discoveredPokemons = emptyList<Int>()
    private lateinit var callbackChoice : (Int) -> Unit
    class PokedexViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val pokemon_name: TextView = view.findViewById(R.id.pokedex_pokemon_name)
        val pokemon_id: TextView = view.findViewById(R.id.pokedex_pokemon_id)
        val pokemon_sprite: ImageView = view.findViewById(R.id.pokedex_pokemon_sprite)
        val pokemon_card : ConstraintLayout = view.findViewById(R.id.pokedex_pokemon)

        val pokeball_owned_icon : ImageView = view.findViewById<ImageView>(R.id.pokeball_owned_icon)
    }

    fun updatePokemonList(pokemonList: List<Pokemon>){
        this.pokemonList = pokemonList
        size = pokemonList.size
    }

    fun updateDiscoveredPokemon(discovered: List<Int>){
        this.discoveredPokemons = discovered
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
        val pokemon = pokemonList[position]

        holder.pokemon_name.text = pokemon.name.capitalize()
        holder.pokemon_id.text = pokemon.formatId(pokemon.id)

        val isUndiscovered = !teamMode && !discoveredPokemons.contains(pokemon.id)
        holder.pokeball_owned_icon.visibility = if (isUndiscovered) View.GONE else View.VISIBLE

        val backgroundRes = if (isUndiscovered) {
            R.drawable.undiscovered_pokemon_container
        } else {
            R.drawable.primary_container
        }
        holder.pokemon_card.background = ContextCompat.getDrawable(context, backgroundRes)

        val textColorAttr = if (isUndiscovered) {
            com.google.android.material.R.attr.colorOnSecondaryContainer
        } else {
            com.google.android.material.R.attr.colorOnPrimaryContainer
        }
        val textColor = ColorHelper.resolveThemeColorAttribute(context, textColorAttr)
        holder.pokemon_name.setTextColor(textColor)


        if(!teamMode) {
            if (discoveredPokemons.contains(pokemon.id)) {
                holder.pokemon_card.setOnClickListener {
                    val popupPokemonDetail = PopupPokemonDetail()
                    popupPokemonDetail.setPokemonToDisplay(
                        pokemon,
                        discoveredPokemons.contains(pokemon.id)
                    )
                    popupPokemonDetail.show(fragmentManager, "popupPokemonDetail")
                }
            }
        }
        else{
            holder.pokemon_card.setOnClickListener{
                callbackChoice(pokemon.id)
            }
        }
        val imageUrl = pokemon.sprites.frontDefault
        Picasso.get().load(imageUrl).into(holder.pokemon_sprite)
        if (isUndiscovered)
        {
            holder.pokemon_sprite.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            holder.pokemon_name.text = "?".repeat(holder.pokemon_name.text.length)
        }
        else
        {
            holder.pokemon_sprite.clearColorFilter()
        }
        val alpha = if (isUndiscovered) 0.55f else 1f
        holder.pokemon_sprite.alpha = alpha
        holder.pokemon_name.alpha = alpha
        holder.pokemon_card.alpha = alpha

    }
}