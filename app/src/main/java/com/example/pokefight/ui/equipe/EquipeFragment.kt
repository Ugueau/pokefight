package com.example.pokefight.ui.equipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentEquipeBinding
import com.example.pokefight.ui.MainViewModel
import androidx.appcompat.app.AppCompatActivity
import com.example.pokefight.model.Pokemon

class EquipeFragment : Fragment() {

    private var _binding: FragmentEquipeBinding? = null
    private var _teamList = mutableListOf<Pokemon>()

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
        val teamAdapter = TeamAdapter(requireContext(), (activity as AppCompatActivity).supportFragmentManager)
        val team = view.findViewById<ListView>(R.id.team)
        team.adapter = teamAdapter

        mainViewModel.getTeam().observe(viewLifecycleOwner){
            _teamList = it.toMutableList()
            teamAdapter.setPokemonList(_teamList)
            teamAdapter.notifyDataSetChanged()
        }

        mainViewModel.teamUpdated.observe(viewLifecycleOwner){
            mainViewModel.getTeam().observe(viewLifecycleOwner){
                _teamList = it.toMutableList()
                teamAdapter.setPokemonList(_teamList)
                teamAdapter.notifyDataSetChanged()
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}