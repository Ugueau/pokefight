package com.example.pokefight.ui.equipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentEquipeBinding
import com.example.pokefight.model.Pokemon
import com.example.pokefight.ui.MainViewModel

class EquipeFragment : Fragment() {

    private var _binding: FragmentEquipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val teamAdapter = TeamAdapter(requireContext())
        val team = view.findViewById<ListView>(R.id.team)
        team.adapter = teamAdapter

        mainViewModel.getPokemonList(1,6).observe(viewLifecycleOwner){
            teamAdapter.setPokemonList(it)
            teamAdapter.notifyDataSetChanged()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}