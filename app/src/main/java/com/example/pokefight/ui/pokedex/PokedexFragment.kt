package com.example.pokefight.ui.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentPokedexBinding
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.ui.ErrorActivity
import com.example.pokefight.ui.MainViewModel

class PokedexFragment : Fragment() {

    private var _binding: FragmentPokedexBinding? = null
    val mainViewModel by activityViewModels<MainViewModel>()

    private val binding get() = _binding!!
    private var isLoading = true

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

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_pokedex)
        val recyclerViewLayoutManager = GridLayoutManager(context, 3)
        val recyclerViewAdapter = PokedexAdapter(requireContext(), emptyList(), (activity as AppCompatActivity).supportFragmentManager)
        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = recyclerViewAdapter


        mainViewModel.checkNetworkConnection(requireContext()).observe(viewLifecycleOwner){isConnected ->
            if(!isConnected){
                val i = Intent(requireContext(), ErrorActivity::class.java)
                startActivity(i)
                activity?.finish()
            }
        }

        isLoading = true
        mainViewModel.getDiscoveredPokemonsIds().observe(viewLifecycleOwner) { dp ->
            mainViewModel.getPokemonList(1, PokemonRepository.getLoadedPokemonAmount())
                .observe(viewLifecycleOwner) {
                    recyclerViewAdapter.updateDiscoveredPokemon(dp)
                    recyclerViewAdapter.updatePokemonList(it.toList())
                    recyclerViewAdapter.notifyDataSetChanged()
                    isLoading = false
                }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = recyclerViewLayoutManager.childCount
                val totalItemCount = recyclerViewLayoutManager.itemCount
                val firstVisibleItemPosition =
                    recyclerViewLayoutManager.findFirstVisibleItemPosition()

                mainViewModel.checkNetworkConnection(requireContext()).observe(viewLifecycleOwner){isConnected ->
                    if(!isConnected){
                        val i = Intent(requireContext(), ErrorActivity::class.java)
                        startActivity(i)
                        activity?.finish()
                    }
                }
                // Check end of the list reached
                if (totalItemCount < PokemonRepository.MAX_ID && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && !isLoading) {
                    // Load more data
                    isLoading = true
                    mainViewModel.getPokemonList(1, PokemonRepository.getLoadedPokemonAmount() + 9)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}