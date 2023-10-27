package com.example.pokefight.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.VIewModel.UserViewModel
import com.example.pokefight.databinding.FragmentHomeBinding
import com.example.pokefight.ui.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val MainViewModel by activityViewModels<MainViewModel>()
    lateinit var vm : MainViewModel
    private lateinit var TextViewTrophy : TextView
    private lateinit var FightButton : Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TextViewTrophy = binding.TextViewTrophy
        TextViewTrophy.text = MainViewModel.getConnectedUserFromCache().Trophy.toString()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}