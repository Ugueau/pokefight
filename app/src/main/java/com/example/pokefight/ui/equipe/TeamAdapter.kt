package com.example.pokefight.ui.equipe

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.pokefight.R
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.formatId
import com.example.pokefight.model.getRarity
import com.example.pokefight.model.getTypeColor
import com.example.pokefight.ui.pokedex.PopupPokemonDetail
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import java.util.concurrent.atomic.AtomicInteger

class TeamAdapter(val context : Context, private val fragmentManager: FragmentManager) : BaseAdapter() {

    private var pokemonList : List<Pokemon> = emptyList()
    private val size = AtomicInteger(0)
    fun setPokemonList(list: List<Pokemon>){
        pokemonList = list
        size.set(pokemonList.size)
    }
    override fun getCount(): Int {
        return size.get()
    }

    override fun getItem(p0: Int): Pokemon {
        return pokemonList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val layout: ConstraintLayout
        val inflater = LayoutInflater.from(context)
        layout = if (view == null) {
            inflater.inflate(R.layout.adapter_team, viewGroup, false) as ConstraintLayout
        } else {
            view as ConstraintLayout
        }

        val pokemon = pokemonList[index]
        val nameAndId = layout.findViewById<TextView>(R.id.team_name);
        nameAndId.text = "${pokemon.name.capitalize()} ${pokemon.formatId(pokemon.id)}"

        val sprite = layout.findViewById<ImageView>(R.id.team_sprite)
        Picasso.get().load(pokemon.sprites.frontDefault).into(sprite)

        val type1 = layout.findViewById<TextView>(R.id.team_type1)
        val label = ContextCompat.getDrawable(context, R.drawable.label)
        val color = ContextCompat.getColor(context, pokemon.getTypeColor(pokemon.types[0]))
        type1.text = pokemon.types[0].type.name.capitalize()
        type1.background = label
        type1.background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        val type2 = layout.findViewById<TextView>(R.id.team_type2)
        if(pokemon.types.size == 2){
            val label1 = ContextCompat.getDrawable(context, R.drawable.label)
            val color2 = ContextCompat.getColor(context, pokemon.getTypeColor(pokemon.types[1]))

            type2.text = pokemon.types[1].type.name.capitalize()
            type2.background = label1
            type2.background?.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP)
        }else{
            type2.text = ""
        }

        val rarity = layout.findViewById<TextView>(R.id.team_rarity)
        rarity.text = pokemon.getRarity().name

        val pokemonClick = layout.findViewById<ConstraintLayout>(R.id.team_pokemon)
        pokemonClick.setOnClickListener{
            val popupPokemonDetail = PopupPokemonDetail()
            popupPokemonDetail.setPokemonToDisplay(pokemon)
            popupPokemonDetail.show(fragmentManager, "popupPokemonDetail")
        }

        val changePokemon = layout.findViewById<FloatingActionButton>(R.id.changePokemonButton)
        changePokemon.setOnClickListener{
            val popup = PopupTeamChoice(index)
            popup.show(fragmentManager, "popupTeamChoice")
        }

        return layout
    }
}