package com.example.pokefight.ui.boutique
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.example.pokefight.R
import com.example.pokefight.model.Pokemon
import com.example.pokefight.ui.MainViewModel
import com.squareup.picasso.Picasso

class BoutiqueFragment : Fragment() {

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm : MainViewModel
    
    //TextView prix dans la boutique
    private lateinit var tvPrixPokemon1 : TextView
    private lateinit var tvPrixPokemon2 : TextView
    private lateinit var tvPrixPokemon3 : TextView
    private lateinit var tvPrixCoffrePokeball : TextView
    private lateinit var tvPrixCoffreSuperball : TextView
    private lateinit var tvPrixCoffreHyperball : TextView
    
    //TextView solde de l'utilisateur
    private lateinit var tvSoldeUser : TextView

    //lineareLayout pokedollard
    private lateinit var layoutPokedollar : LinearLayout

    //imageview des pokemons
    private lateinit var SpritePokemon1 : ImageView
    private lateinit var SpritePokemon2 : ImageView
    private lateinit var SpritePokemon3 : ImageView

    private lateinit var NomPokemon1 : TextView
    private lateinit var NomPokemon2 : TextView
    private lateinit var NomPokemon3 : TextView

    companion object {
        fun newInstance() = BoutiqueFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_boutique, container, false)

        SpritePokemon1 = view.findViewById(R.id.SpritePokemon1)
        SpritePokemon2 = view.findViewById(R.id.SpritePokemon2)
        SpritePokemon3 = view.findViewById(R.id.SpritePokemon3)

        NomPokemon1 = view.findViewById(R.id.NomPokemon1)
        NomPokemon2 = view.findViewById(R.id.NomPokemon2)
        NomPokemon3 = view.findViewById(R.id.NomPokemon3)

        loadPokemonBoutique(
            commonCallback = ::reloadPokemonCommon,
            uncommonCallback = ::reloadPokemonUncommon,
            rareCallback = ::reloadPokemonRare
        )

        tvPrixCoffrePokeball = view.findViewById(R.id.prix_coffre_pokeball)
        tvPrixCoffreSuperball = view.findViewById(R.id.prix_coffre_superball)
        tvPrixCoffreHyperball = view.findViewById(R.id.prix_coffre_hyperball)
        tvPrixPokemon1 = view.findViewById(R.id.pokemonPrix1)
        tvPrixPokemon2 = view.findViewById(R.id.pokemonPrix2)
        tvPrixPokemon3 = view.findViewById(R.id.pokemonPrix3)

        //insert des prix des coffres
        tvPrixCoffrePokeball.text = vm.prix_boutique["POKEBALL"].toString()
        tvPrixCoffreSuperball.text = vm.prix_boutique["SUPERBALL"].toString()
        tvPrixCoffreHyperball.text = vm.prix_boutique["HYPERBALL"].toString()

        //insert des prix des cartes
        tvPrixPokemon1.text = vm.prix_boutique["COMMON"].toString()
        tvPrixPokemon2.text = vm.prix_boutique["UNCOMMON"].toString()
        tvPrixPokemon3.text = vm.prix_boutique["RARE"].toString()

        //insert du solde de l'utilisateur
        tvSoldeUser = view.findViewById(R.id.soldeUser)
        tvSoldeUser.text = vm.getConnectedUser().pokedollar.toString()

        //création de la popup pour acheter des pokedollard
        layoutPokedollar = view.findViewById(R.id.layoutPokedollar)
        layoutPokedollar.setOnClickListener { click -> this.showPopupPokedollar() }

        // instentiation des layout d'achat du fragment
        // première ligne pour l'achat de pokemon
        val layoutCarte1 = (view.findViewById(R.id.layoutCarte1)as ConstraintLayout)
        layoutCarte1.setOnClickListener {

            val id = vm.pokemon_boutique["COMMON"]

            if (id != null){
                lateinit var pokemon: Pokemon
                vm.getPokemonById(id).observe(viewLifecycleOwner){
                    if (it != null) {
                        pokemon = it
                        showPopupConfirmAchat(
                            vm.prix_boutique.keys.elementAt(0),
                            pokemon
                        )
                    }
                }


            }
            else{
                Toast.makeText(
                    context,
                    "Pokemon not allready loaded",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        val layoutCarte2 = (view.findViewById(R.id.layoutCarte2)as ConstraintLayout)
        layoutCarte2.setOnClickListener {
            val id = vm.pokemon_boutique["UNCOMMON"]

            if (id != null){
                lateinit var pokemon: Pokemon
                vm.getPokemonById(id).observe(viewLifecycleOwner){
                    if (it != null) {
                        pokemon = it
                        showPopupConfirmAchat(
                            vm.prix_boutique.keys.elementAt(1),
                            pokemon
                        )
                    }
                }
            }
            else{
                Toast.makeText(
                    context,
                    "Pokemon not allready loaded",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        val layoutCarte3 = (view.findViewById(R.id.layoutCarte3)as ConstraintLayout)
        layoutCarte3.setOnClickListener {
            val id = vm.pokemon_boutique["RARE"]

            if (id != null){
                lateinit var pokemon: Pokemon
                vm.getPokemonById(id).observe(viewLifecycleOwner){
                    if (it != null) {
                        pokemon = it
                        showPopupConfirmAchat(
                            vm.prix_boutique.keys.elementAt(2),
                            pokemon
                        )
                    }
                }
            }
            else{
                Toast.makeText(
                    context,
                    "Pokemon not allready loaded",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        // deuxième ligne pour l'achat de coffre
        val layoutPokeball = (view.findViewById(R.id.layoutPokeball)as ConstraintLayout)
        layoutPokeball.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(3),
                null
            )
        }

        val layoutSuperball = (view.findViewById(R.id.layoutSuperball)as ConstraintLayout)
        layoutSuperball.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(4),
                null
            )
        }

        val layoutHyperball = (view.findViewById(R.id.layoutHyperball)as ConstraintLayout)
        layoutHyperball.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(5),
                null
            )
        }

        return view;
    }

    private fun showPopupPokedollar(){
        val popupPokedollar = PopupPokedollar(){reloadSolde()}
        popupPokedollar.show((activity as AppCompatActivity).supportFragmentManager, "popupPokedollar")
    }

    private fun showPopupConfirmAchat( key : String, pokemon: Pokemon?){
        val popupConfirmAchat = PopupConfirmAchat(
            key,
            pokemon,
            endPurchase = ::reloadSolde
        )
        popupConfirmAchat.show((activity as AppCompatActivity).supportFragmentManager, "popupConfirmAchat")
    }

    fun reloadSolde(){
        tvSoldeUser.text = vm.getConnectedUser().pokedollar.toString()
    }

    fun reloadPokemonCommon(){
        lateinit var common : Pokemon
        vm.getPokemonById(vm.pokemon_boutique["COMMON"]!!).observe(viewLifecycleOwner){
            if (it == null){
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            }
            else{
                common = it
                val imageUrlCommon = common.sprites.frontDefault
                Picasso.get().load(imageUrlCommon).into(SpritePokemon1)
                NomPokemon1.text = common.name
            }
        }

    }

    fun reloadPokemonUncommon(){
        lateinit var uncommon : Pokemon
        vm.getPokemonById(vm.pokemon_boutique["UNCOMMON"]!!).observe(viewLifecycleOwner){
            if (it == null){
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            }
            else{
                uncommon = it
                val imageUrlUncommon = uncommon.sprites.frontDefault
                Picasso.get().load(imageUrlUncommon).into(SpritePokemon2)
                NomPokemon2.text = uncommon.name
            }
        }
    }

    fun reloadPokemonRare(){
        lateinit var rare : Pokemon
        vm.getPokemonById(vm.pokemon_boutique["RARE"]!!).observe(viewLifecycleOwner){
            if (it == null){
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            }
            else{
                rare = it
                val imageUrlRare = rare.sprites.frontDefault
                Picasso.get().load(imageUrlRare).into(SpritePokemon3)
                NomPokemon3.text = rare.name
            }
        }
    }

    fun loadPokemonBoutique(commonCallback: () -> Unit, uncommonCallback: () -> Unit, rareCallback: () -> Unit){
        // set des pokemons de la boutique
        lateinit var common : Pokemon
        lateinit var uncommon : Pokemon
        lateinit var rare : Pokemon


        // common
        vm.generatePokemonCommonBoutique().observe(viewLifecycleOwner){ it ->
            if (it == null){
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            }
            else{
                common = it
                vm.SetBoutiquePokemonComon(common.id)
                commonCallback()
            }
        }

        //uncommon
        vm.generatePokemonUncommonBoutique().observe(viewLifecycleOwner){ it ->
            if (it == null){
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            }
            else{
                uncommon = it
                vm.SetBoutiquePokemonUncomon(uncommon.id)
                uncommonCallback()
            }
        }

        //rare
        vm.generatePokemonRareBoutique().observe(viewLifecycleOwner){ it ->
            if (it == null){
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            }
            else{
                rare = it
                vm.SetBoutiquePokemonRare(rare.id)
                rareCallback()
            }
        }
    }
}