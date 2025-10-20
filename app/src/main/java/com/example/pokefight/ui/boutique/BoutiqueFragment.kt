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
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.example.pokefight.R
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.Rarity
import com.example.pokefight.model.getRarity
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

    private lateinit var RarityPokeon1 : ImageView
    private lateinit var RarityPokeon2 : ImageView
    private lateinit var RarityPokeon3 : ImageView

    private lateinit var NomPokemon1 : TextView
    private lateinit var NomPokemon2 : TextView
    private lateinit var NomPokemon3 : TextView

    private var isAchatConfirmed = false

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

        RarityPokeon1 = view.findViewById(R.id.RarityPokemon1)
        RarityPokeon2 = view.findViewById(R.id.RarityPokemon2)
        RarityPokeon3 = view.findViewById(R.id.RarityPokemon3)

        NomPokemon1 = view.findViewById(R.id.NomPokemon1)
        NomPokemon2 = view.findViewById(R.id.NomPokemon2)
        NomPokemon3 = view.findViewById(R.id.NomPokemon3)

        vm.getDraws().observe(viewLifecycleOwner){ pokemonDraw ->
            loadPokemonCommon(pokemonDraw.id1)
            loadPokemonUncommon(pokemonDraw.id2)
            loadPokemonRare(pokemonDraw.id3)
            val idCommon = pokemonDraw.id1
            val idUncommon = pokemonDraw.id2
            val idRare = pokemonDraw.id3
            // instentiation des layout d'achat du fragment
            // première ligne pour l'achat de pokemon
            val layoutCarte1 = (view.findViewById(R.id.layoutCarte1)as ConstraintLayout)
            layoutCarte1.setOnClickListener {
                lateinit var pokemon: Pokemon
                vm.getPokemonById(idCommon).observe(viewLifecycleOwner){
                    if (it != null) {
                        pokemon = it
                        showPopupConfirmAchat(
                            vm.prix_boutique.keys.elementAt(0),
                            listOf<Pokemon>(pokemon)
                        )
                    }
                }
            }

            val layoutCarte2 = (view.findViewById(R.id.layoutCarte2)as ConstraintLayout)
            layoutCarte2.setOnClickListener {
                lateinit var pokemon: Pokemon
                vm.getPokemonById(idUncommon).observe(viewLifecycleOwner) {
                    if (it != null) {
                        pokemon = it
                        showPopupConfirmAchat(
                            vm.prix_boutique.keys.elementAt(1),
                            listOf<Pokemon>(pokemon)
                        )
                    }
                }
            }

            val layoutCarte3 = (view.findViewById(R.id.layoutCarte3)as ConstraintLayout)
            layoutCarte3.setOnClickListener {
                lateinit var pokemon: Pokemon
                vm.getPokemonById(idRare).observe(viewLifecycleOwner) {
                    if (it != null) {
                        pokemon = it
                        showPopupConfirmAchat(
                            vm.prix_boutique.keys.elementAt(2),
                            listOf<Pokemon>(pokemon)
                        )
                    }
                }
            }
        }

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


        // deuxième ligne pour l'achat de coffre
        val layoutPokeball = (view.findViewById(R.id.layoutPokeball)as ConstraintLayout)
        layoutPokeball.setOnClickListener {
            val random = java.util.Random()
            val randomPokemonId = random.nextInt(PokemonRepository.MAX_ID) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151
            val pokemons = mutableListOf<Pokemon>()
            vm.getPokemonById(randomPokemonId).observe(viewLifecycleOwner){
                if (it != null) {
                    pokemons.add(it)
                    showPopupConfirmAchat(
                        vm.prix_boutique.keys.elementAt(3),
                        pokemons
                    )
                }
            }
        }

        val layoutSuperball = (view.findViewById(R.id.layoutSuperball)as ConstraintLayout)
        layoutSuperball.setOnClickListener {
            val pokemons = mutableListOf<Pokemon>()
            val pokemonIds = mutableListOf<Int>()
            for (i in 0..1){
                val random = java.util.Random()
                val randomPokemonId = random.nextInt(PokemonRepository.MAX_ID) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151
                pokemonIds.add(randomPokemonId)
            }
            vm.getPokemonListByIds(pokemonIds).observe(viewLifecycleOwner){ pokemonList ->
                pokemons.addAll(pokemonList)
                showPopupConfirmAchat(
                    vm.prix_boutique.keys.elementAt(4),
                    pokemons
                )
            }
        }

        val layoutHyperball = (view.findViewById(R.id.layoutHyperball)as ConstraintLayout)
        layoutHyperball.setOnClickListener {
            val pokemons = mutableListOf<Pokemon>()
            val pokemonIds = mutableListOf<Int>()
            for (i in 0..2){
                val random = java.util.Random()
                val randomPokemonId = random.nextInt(PokemonRepository.MAX_ID) + 1 // random number betwin 0 & 150 + 1 to start at 1 and finish at 151
                pokemonIds.add(randomPokemonId)
            }
            vm.getPokemonListByIds(pokemonIds).observe(viewLifecycleOwner){ pokemonList ->
                pokemons.addAll(pokemonList)
                showPopupConfirmAchat(
                    vm.prix_boutique.keys.elementAt(5),
                    pokemons
                )
            }
        }

        return view;
    }

    private fun showPopupPokedollar(){
        val popupPokedollar = PopupPokedollar(){reloadSolde()}
        popupPokedollar.show((activity as AppCompatActivity).supportFragmentManager, "popupPokedollar")
    }

    private fun showPopupConfirmAchat( key : String, pokemons: List<Pokemon>){
        val popupConfirmAchat = PopupConfirmAchat(
            key,
            pokemons,
            endPurchase = ::reloadSolde
        )
        popupConfirmAchat.show((activity as AppCompatActivity).supportFragmentManager, "popupConfirmAchat")
    }

    fun reloadSolde(){
        tvSoldeUser.text = vm.getConnectedUser().pokedollar.toString()
    }

    fun loadPokemonCommon(pokemonId : Int){
        lateinit var common : Pokemon
        vm.getPokemonById(pokemonId).observe(viewLifecycleOwner){
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

    fun loadPokemonUncommon(pokemonId : Int){
        lateinit var uncommon : Pokemon
        vm.getPokemonById(pokemonId).observe(viewLifecycleOwner){
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

    fun loadPokemonRare(pokemonId : Int){
        lateinit var rare : Pokemon
        vm.getPokemonById(pokemonId).observe(viewLifecycleOwner){
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

}