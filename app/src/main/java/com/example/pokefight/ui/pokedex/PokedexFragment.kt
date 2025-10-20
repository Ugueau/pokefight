package com.example.pokefight.ui.pokedex

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentPokedexBinding
import com.example.pokefight.domain.PokemonRepository
import com.example.pokefight.ui.ErrorActivity
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

class PokedexFragment : Fragment() {

    private var _binding: FragmentPokedexBinding? = null
    val mainViewModel by activityViewModels<MainViewModel>()

    private val binding get() = _binding!!
    private var filterString : String = ""
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_pokedex)
        val recyclerViewLayoutManager = GridLayoutManager(context, 3)
        val recyclerViewAdapter = PokedexAdapter(requireContext(), emptyList(), (activity as AppCompatActivity).supportFragmentManager)
        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = recyclerViewAdapter

        val searchBarInput = view.findViewById<EditText>(R.id.search_bar)
        searchBarInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchBarInput.setText("")
                filterString = ""
            }
            else
            {
                mainViewModel.getPokemonList(1, PokemonRepository.getLoadedPokemonAmount()).observe(viewLifecycleOwner) { pokemons ->
                    filterString = searchBarInput.text.toString()
                    val filteredPokemons =  if (filterString != "") pokemons.filter { it.name.contains(this.filterString) } else pokemons
                    recyclerViewAdapter.updatePokemonList(filteredPokemons.toList())
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
        val rootLayout = view.findViewById<ConstraintLayout>(R.id.pokedex_root_layout) // your root layout id

        rootLayout.setOnTouchListener { _, _ ->
            if (searchBarInput.isFocused) {
                searchBarInput.clearFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchBarInput.windowToken, 0)
            }
            false
        }

        recyclerView.setOnTouchListener { _, _ ->
            if (searchBarInput.isFocused) {
                searchBarInput.clearFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchBarInput.windowToken, 0)
            }
            false
        }


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
                .observe(viewLifecycleOwner) { pokemons ->
                    val filteredPokemons =  if (filterString != "") pokemons.filter { it.name.contains(this.filterString) } else pokemons
                    recyclerViewAdapter.updateDiscoveredPokemon(dp)
                    recyclerViewAdapter.updatePokemonList(filteredPokemons.toList())
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
                        .observe(viewLifecycleOwner) { pokemons ->
                            isLoading = false
                            val filteredPokemons =  if (filterString != "") pokemons.filter { it.name.contains(filterString) } else pokemons
                            recyclerViewAdapter.updatePokemonList(filteredPokemons.toList())
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