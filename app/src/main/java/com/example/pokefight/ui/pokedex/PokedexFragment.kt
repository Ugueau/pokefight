package com.example.pokefight.ui.pokedex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentPokedexBinding
import com.example.pokefight.model.Pokemon
import com.example.pokefight.ui.PokemonViewModel

class PokedexFragment : Fragment() {

    private var _binding: FragmentPokedexBinding? = null
    val pokemonViewModel by activityViewModels<PokemonViewModel>()
    lateinit var vm : PokemonViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokedexBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_pokedex)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        pokemonViewModel.getPokemonList(1,10).observe(this){pokemonList ->
            recyclerView.adapter = PokedexAdapter(requireContext(), pokemonList)
        }

        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}