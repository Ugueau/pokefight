package com.example.pokefight.ui.boutique

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
    val pokemon: Pokemon?,
    val endPurchase : () -> Unit) : DialogFragment() {

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm: MainViewModel

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
                    val imageUrlRare = pokemon.sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(imageProduct)
                    productName.text = pokemon.name

                    imageProduct.scaleX = 2F
                    imageProduct.scaleY = 2F
                }
            }

            "UNCOMMON" -> {
                if (pokemon != null) {
                    val imageUrlRare = pokemon.sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(imageProduct)
                    productName.text = pokemon.name

                    imageProduct.scaleX = 2F
                    imageProduct.scaleY = 2F
                }
            }

            "RARE" -> {
                if (pokemon != null) {
                    val imageUrlRare = pokemon.sprites.frontDefault
                    Picasso.get().load(imageUrlRare).into(imageProduct)
                    productName.text = pokemon.name

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

            confirmPurchase()

        }

        //cancel the purchase
        val cancelbutton = view.findViewById<Button>(R.id.cancelbutton)
        cancelbutton.setOnClickListener { _ ->

            dismiss()
        }

    }

    private fun confirmPurchase() {

        //get the price
        val prix = vm.prix_boutique[key]

        if (prix != null) {
            // check if the user can purchase the product
            if (vm.getConnectedUser().pokedollar >= prix) {
                when (key) {
                    "COMMON" -> {
                        caseCommon(-prix)
                    }

                    "UNCOMMON" -> {
                        caseUncommon(-prix)
                    }

                    "RARE" -> {
                        caseRare(-prix)
                    }

                    "POKEBALL" -> {
                        casePokeball(-prix)
                    }

                    "SUPERBALL" -> {
                        caseSuperball(-prix)
                    }

                    "HYPERBALL" -> {
                        caseHyperball(-prix)
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Not enough Pokedollar",
                    Toast.LENGTH_SHORT,
                ).show()
                endPurchase()
                dismiss()
            }
        }
    }

    private fun caseCommon(prix: Int) {
        if (pokemon != null) {
            vm.addToDiscoveredPokemon(listOf<Int>(pokemon.id))
            vm.updateUserSolde(prix)

            Toast.makeText(
                context,
                "${pokemon.name} successfully purchase",
                Toast.LENGTH_SHORT,
            ).show()
            endPurchase()
            dismiss()
        }
    }

    private fun caseUncommon(prix: Int) {
        if (pokemon != null) {
            vm.addToDiscoveredPokemon(listOf<Int>(pokemon.id))
            vm.updateUserSolde(prix)

            Toast.makeText(
                context,
                "${pokemon.name} successfully purchase",
                Toast.LENGTH_SHORT,
            ).show()
            endPurchase()
            dismiss()
        }
    }

    private fun caseRare(prix: Int) {
        if (pokemon != null) {
            vm.addToDiscoveredPokemon(listOf<Int>(pokemon.id))
            vm.updateUserSolde(prix)

            Toast.makeText(
                context,
                "${pokemon.name} successfully purchase",
                Toast.LENGTH_SHORT,
            ).show()
            endPurchase()
            dismiss()
        }
    }

    private fun casePokeball(prix: Int) {
        val random = java.util.Random()
        val randomPokemonId =
            random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

        vm.addToDiscoveredPokemon(listOf<Int>(randomPokemonId))

        val intent = Intent(context, DiscoveredPokemonActivity::class.java)
        intent.putExtra("POKEMON_POKEBALL", randomPokemonId)

        intent.putExtra("PURCHASE" ,"POKEBALL")

        vm.updateUserSolde(prix)

        Toast.makeText(
            context,
            "Pokeball chest successfully purchase",
            Toast.LENGTH_SHORT,
        ).show()

        endPurchase()

        startActivity(intent)
        dismiss()
    }

    private fun caseSuperball(prix: Int) {
        val random = java.util.Random()
        val randomPokemonPokeball =
            random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

        val intent = Intent(context, DiscoveredPokemonActivity::class.java)
        intent.putExtra("POKEMON_POKEBALL", randomPokemonPokeball)

        val randomPokemonSuperball =
            random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

        vm.addToDiscoveredPokemon(listOf<Int>(randomPokemonPokeball,randomPokemonSuperball))

        intent.putExtra("POKEMON_SUPERBALL", randomPokemonSuperball)

        intent.putExtra("PURCHASE" ,"SUPERBALL")

        vm.updateUserSolde(prix)

        Toast.makeText(
            context,
            "Superball chest successfully purchase",
            Toast.LENGTH_SHORT,
        ).show()

        endPurchase()

        startActivity(intent)
        dismiss()
    }

    private fun caseHyperball(prix: Int) {
        val random = java.util.Random()
        val randomPokemonPokeball =
            random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

        val intent = Intent(context, DiscoveredPokemonActivity::class.java)
        intent.putExtra("POKEMON_POKEBALL", randomPokemonPokeball)

        val randomPokemonSuperball =
            random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

        intent.putExtra("POKEMON_SUPERBALL", randomPokemonSuperball)

        val randomPokemonHyperball =
            random.nextInt(151) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151

        vm.addToDiscoveredPokemon(listOf<Int>(randomPokemonPokeball,randomPokemonSuperball,randomPokemonHyperball))

        intent.putExtra("POKEMON_HYPERBALL", randomPokemonHyperball)

        intent.putExtra("PURCHASE" ,"HYPERBALL")

        vm.updateUserSolde(prix)

        Toast.makeText(
            context,
            "Hyperball chest successfully purchase",
            Toast.LENGTH_SHORT,
        ).show()

        endPurchase()

        startActivity(intent)
        dismiss()
    }
}