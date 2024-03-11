package com.example.pokefight.ui.pokedex

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.pokefight.R
import com.example.pokefight.model.Attribute
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.formatId
import com.example.pokefight.model.getAttribute
import com.example.pokefight.model.getRarity
import com.example.pokefight.model.getTypeColor
import com.squareup.picasso.Picasso

class  PopupPokemonDetail : DialogFragment() {
    lateinit var pokemon: Pokemon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_popup_pokemon_detail, container, false)
        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return v
    }

    fun setPokemonToDisplay(pokemonToDisplay: Pokemon){
        pokemon = pokemonToDisplay
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popup);
        close.setOnClickListener {
            dismiss()
        }

        val sprite = view.findViewById<ImageView>(R.id.pokedex_detail_sprite)
        val imageUrl = pokemon.sprites.frontDefault
        Picasso.get().load(imageUrl).into(sprite)

        val nameAndId = view.findViewById<TextView>(R.id.pokedex_detail_name)
        val type1 = view.findViewById<TextView>(R.id.pokedex_detail_type1)
        val type2 = view.findViewById<TextView>(R.id.pokedex_detail_type2)
        val hp = view.findViewById<TextView>(R.id.pokedex_detail_hp)
        val attack = view.findViewById<TextView>(R.id.pokedex_detail_attack)
        val defense = view.findViewById<TextView>(R.id.pokedex_detail_defense)
        val rarity = view.findViewById<TextView>(R.id.pokedex_rarity)

        nameAndId.text = "${pokemon.name.capitalize()} ${pokemon.formatId(pokemon.id)}"

        val label = ContextCompat.getDrawable((activity as AppCompatActivity).baseContext, R.drawable.label)
        val color = ContextCompat.getColor((activity as AppCompatActivity).baseContext, pokemon.getTypeColor(pokemon.types[0]))

        type1.text = pokemon.types[0].type.name.capitalize()
        type1.background = label
        type1.background?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        if(pokemon.types.size == 2){
            val label2 = ContextCompat.getDrawable((activity as AppCompatActivity).baseContext, R.drawable.label)
            val color2 = ContextCompat.getColor((activity as AppCompatActivity).baseContext, pokemon.getTypeColor(pokemon.types[1]))

            type2.text = pokemon.types[1].type.name.capitalize()
            type2.background = label2
            type2.background?.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP)
        }else{
            type2.text = ""
        }

        hp.text = "HP ${pokemon.getAttribute(Attribute.HP).toString()}"
        attack.text = pokemon.getAttribute(Attribute.ATTACK).toString()
        defense.text = pokemon.getAttribute(Attribute.DEFENSE).toString()

        rarity.text = pokemon.getRarity().name
    }

}