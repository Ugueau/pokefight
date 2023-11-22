package com.example.pokefight.ui.pokedex

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.pokefight.R
import com.example.pokefight.model.Attribute
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.formatId
import com.example.pokefight.model.getAttribute
import com.squareup.picasso.Picasso

class PopupPokemonDetail : DialogFragment() {
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
        return v;
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

        nameAndId.text = "${pokemon.name} ${pokemon.formatId(pokemon.id)}"
        type1.text = pokemon.types[0].type.name
        if(pokemon.types.size == 2){
            type2.text = pokemon.types[1].type.name
        }else{
            type2.text = ""
        }

        hp.text = "HP ${pokemon.getAttribute(pokemon, Attribute.HP).toString()}"
        attack.text = pokemon.getAttribute(pokemon, Attribute.ATTACK).toString()
        defense.text = pokemon.getAttribute(pokemon, Attribute.DEFENSE).toString()
    }

}