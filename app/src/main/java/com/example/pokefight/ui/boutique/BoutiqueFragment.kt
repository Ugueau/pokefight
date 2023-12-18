package com.example.pokefight.ui.boutique

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel
import com.example.pokefight.ui.pokedex.PopupPokemonDetail

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
        tvSoldeUser.text = vm.getConnectedUserFromCache().pokedollar.toString()

        //création de la popup pour acheter des pokedollard
        layoutPokedollar = view.findViewById(R.id.layoutPokedollar)
        layoutPokedollar.setOnClickListener { click -> this.showPopupPokedollar() }

        // instentiation des layout d'achat du fragment
        // première ligne pour l'achat de pokemon
        val layoutCarte1 = (view.findViewById(R.id.layoutCarte1)as ConstraintLayout)
        layoutCarte1.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(0),
                view.findViewById(R.id.DetailPokemon1)
            )
        }

        val layoutCarte2 = (view.findViewById(R.id.layoutCarte2)as ConstraintLayout)
        layoutCarte2.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(1),
                view.findViewById(R.id.DetailPokemon2)
            )
        }

        val layoutCarte3 = (view.findViewById(R.id.layoutCarte3)as ConstraintLayout)
        layoutCarte3.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(2),
                view.findViewById(R.id.DetailPokemon3)
            )
        }

        // deuxième ligne pour l'achat de coffre
        val layoutPokeball = (view.findViewById(R.id.layoutPokeball)as ConstraintLayout)
        layoutPokeball.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(3),
                view.findViewById(R.id.layoutImagePokeball)
            )
        }

        val layoutSuperball = (view.findViewById(R.id.layoutSuperball)as ConstraintLayout)
        layoutSuperball.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(4),
                view.findViewById(R.id.layoutSuperball)
            )
        }

        val layoutHyperball = (view.findViewById(R.id.layoutHyperball)as ConstraintLayout)
        layoutHyperball.setOnClickListener {
            showPopupConfirmAchat(
                vm.prix_boutique.keys.elementAt(5),
                view.findViewById(R.id.layoutHyperball)
            )
        }

        return view;
    }

    private fun showPopupPokedollar(){
        val popupPokedollar = PopupPokedollar(){reloadSolde()}
        popupPokedollar.show((activity as AppCompatActivity).supportFragmentManager, "popupPokedollar")
    }

    private fun showPopupConfirmAchat( key : String, layout: ConstraintLayout){
        val popupConfirmAchat = PopupConfirmAchat(key, layout){reloadSolde()}
        popupConfirmAchat.show((activity as AppCompatActivity).supportFragmentManager, "popupConfirmAchat")
    }

    fun reloadSolde(){
        tvSoldeUser.text = vm.getConnectedUserFromCache().pokedollar.toString()
    }
}