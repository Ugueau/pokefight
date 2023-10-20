package com.example.pokefight.ui.pokedex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.databinding.FragmentPokedexBinding

class PokedexFragment : Fragment() {

    private var _binding: FragmentPokedexBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pokedexViewModel =
            ViewModelProvider(this).get(PokedexViewModel::class.java)

        _binding = FragmentPokedexBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPokedex
        pokedexViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}