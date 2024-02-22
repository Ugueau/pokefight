package com.example.pokefight.ui.swap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.ui.MainViewModel
import com.example.pokefight.ui.pokedex.PokedexAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PopupPokemonSelect() : BottomSheetDialogFragment() {
    private var isLoading = true

    val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popup_team_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_pokedex)

        val recyclerViewLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = recyclerViewLayoutManager

        val recyclerViewAdapter = PokedexAdapter(
            requireContext(),
            emptyList(),
            (activity as AppCompatActivity).supportFragmentManager
        )
        recyclerViewAdapter.setToTeamChoiceMode() { pokemonId ->
            selectedPokemon(pokemonId)
        }
        recyclerView.adapter = recyclerViewAdapter

        isLoading = true
        mainViewModel.getDiscoveredPokemons()
            .observe(viewLifecycleOwner) {
                isLoading = false
                recyclerViewAdapter.updatePokemonList(it.toList())
                recyclerViewAdapter.notifyDataSetChanged()
            }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = recyclerViewLayoutManager.childCount
                val totalItemCount = recyclerViewLayoutManager.itemCount
                val firstVisibleItemPosition =
                    recyclerViewLayoutManager.findFirstVisibleItemPosition()

                // Check end of the list reached
                if (totalItemCount < PokemonRepository.MAX_ID && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && !isLoading) {
                    // Load more data
                    isLoading = true
                    mainViewModel.getDiscoveredPokemons()
                        .observe(viewLifecycleOwner) {
                            isLoading = false
                            recyclerViewAdapter.updatePokemonList(it.toList())
                            recyclerViewAdapter.notifyDataSetChanged()
                        }
                }

            }
        })

        //Useful for optimization
        recyclerView.setHasFixedSize(true)
    }

    fun selectedPokemon(pokemonId: Int) {
        mainViewModel.sendPokemonToSwap(pokemonId)
        mainViewModel.setPokemonSelectedForSwap(pokemonId)
        dismiss()
    }

}