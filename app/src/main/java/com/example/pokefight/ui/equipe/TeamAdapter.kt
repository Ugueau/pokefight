package com.example.pokefight.ui.equipe

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.pokefight.R
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.formatId
import com.example.pokefight.model.getRarity
import com.example.pokefight.model.getTypeColor
import com.squareup.picasso.Picasso

class TeamAdapter(val context : Context, val pokemonList : List<Pokemon>) : BaseAdapter() {
    override fun getCount(): Int {
        return pokemonList.size
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
        nameAndId.text = "${pokemon.name} ${pokemon.formatId(pokemon.id)}"

        val sprite = layout.findViewById<ImageView>(R.id.team_sprite)
        Picasso.get().load(pokemon.sprites.frontDefault).into(sprite)

        val type1 = layout.findViewById<TextView>(R.id.team_type1)
        val label = ContextCompat.getDrawable(context, R.drawable.label)
        var color = ContextCompat.getColor(context, pokemon.getTypeColor(pokemon.types[0]))
        type1.text = pokemon.types[0].type.name
        type1.background = label
        type1.background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        val type2 = layout.findViewById<TextView>(R.id.team_type2)
        if(pokemon.types.size == 2){
            color = ContextCompat.getColor(context, pokemon.getTypeColor(pokemon.types[1]))

            type2.text = pokemon.types[1].type.name
            type2.background = label
            type2.background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }else{
            type2.text = ""
        }

        val rarity = layout.findViewById<TextView>(R.id.team_rarity)
        rarity.text = pokemon.getRarity().name

        return layout
    }
}