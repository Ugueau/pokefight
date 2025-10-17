package com.example.pokefight.ui.boutique

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.DiscoveredPokemonActivity
import com.example.pokefight.R
import com.example.pokefight.model.Pokemon
import com.example.pokefight.ui.MainViewModel
import com.squareup.picasso.Picasso

class PopupConfirmAchat(
    val key : String,
    val pokemon: List<Pokemon>,
    val endPurchase : () -> Unit) : DialogFragment() {

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm: MainViewModel

    interface OnPopupConfirmAchatListener {
        fun onPopupConfirmAchatResult(isAchatConfirmed: Boolean, pokemons : List<Pokemon>, prix : Int)
    }

    private var listener: OnPopupConfirmAchatListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnPopupConfirmAchatListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }

        return inflater.inflate(R.layout.fragment_popup_confirm_achat, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popup3);
        close.setOnClickListener {
            dismiss()
        }

        vm = ViewModelProvider(this)[MainViewModel::class.java]

        val imageProduct: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)

        when (key) {
            "COMMON" -> {
                if (pokemon != null) {
                    val imageUrlRare = pokemon[0].sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(imageProduct)
                    productName.text = pokemon[0].name

                    imageProduct.scaleX = 2F
                    imageProduct.scaleY = 2F
                }
            }

            "UNCOMMON" -> {
                if (pokemon != null) {
                    val imageUrlRare = pokemon[0].sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(imageProduct)
                    productName.text = pokemon[0].name

                    imageProduct.scaleX = 2F
                    imageProduct.scaleY = 2F
                }
            }

            "RARE" -> {
                if (pokemon != null) {
                    val imageUrlRare = pokemon[0].sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(imageProduct)
                    productName.text = pokemon[0].name

                    imageProduct.scaleX = 2F
                    imageProduct.scaleY = 2F
                }
            }

            "POKEBALL" -> {
                //adding the image of the product
                imageProduct.setImageResource(R.mipmap.coffre_pokeball_foreground)
                // value the name of the product
                productName.text = getString(R.string.get_1_random_pokemon)
            }

            "SUPERBALL" -> {
                //adding the image of the product
                imageProduct.setImageResource(R.mipmap.coffre_superball_foreground)
                // value the name of the product
                productName.text = getString(R.string.get_2_random_pokemons)
            }

            "HYPERBALL" -> {
                //adding the image of the product
                imageProduct.setImageResource(R.mipmap.coffre_hyperball_foreground)
                // value the name of the product
                productName.text = getString(R.string.get_3_random_pokemons)
            }
        }

        //confirm the purchase
        val confirmButton = view.findViewById<Button>(R.id.confirmButton)

        confirmButton.setOnClickListener { _ ->

            //get the price
            val prix = vm.prix_boutique[key]

            if (prix != null) {
                // check if the user can purchase the product
                if (vm.getConnectedUser().pokedollar >= prix) {
                    Toast.makeText(
                        context,
                        "Successfully purchased",
                        Toast.LENGTH_SHORT,
                    ).show()
                    listener?.onPopupConfirmAchatResult(true, pokemon, prix)
                    if (key == "POKEBALL")
                    {
                        val intent = Intent(context, DiscoveredPokemonActivity::class.java)
                        intent.putExtra("POKEMON_POKEBALL", pokemon[0].id)
                        intent.putExtra("PURCHASE" ,"POKEBALL")
                        startActivity(intent)
                    }
                    else if (key == "SUPERBALL")
                    {
                        val intent = Intent(context, DiscoveredPokemonActivity::class.java)
                        intent.putExtra("POKEMON_POKEBALL", pokemon[0].id)
                        intent.putExtra("POKEMON_SUPERBALL", pokemon[1].id)
                        intent.putExtra("PURCHASE" ,"SUPERBALL")
                        startActivity(intent)
                    }
                    else if (key == "HYPERBALL")
                    {
                        val intent = Intent(context, DiscoveredPokemonActivity::class.java)
                        intent.putExtra("POKEMON_POKEBALL", pokemon[0].id)
                        intent.putExtra("POKEMON_SUPERBALL", pokemon[1].id)
                        intent.putExtra("POKEMON_HYPERBALL", pokemon[2].id)
                        intent.putExtra("PURCHASE" ,"HYPERBALL")
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Not enough Pokedollar",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            endPurchase()
            dismiss()
        }

        //cancel the purchase
        val cancelbutton = view.findViewById<Button>(R.id.cancelbutton)
        cancelbutton.setOnClickListener { _ ->
            dismiss()
        }

    }
}