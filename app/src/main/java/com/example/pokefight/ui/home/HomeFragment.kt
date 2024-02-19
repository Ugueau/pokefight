package com.example.pokefight.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokefight.R
import com.example.pokefight.databinding.FragmentHomeBinding
import com.example.pokefight.domain.firebase.DSRealTimeDatabase
import com.example.pokefight.ui.MainViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val mainViewModel by activityViewModels<MainViewModel>()
    lateinit var vm : MainViewModel
    private lateinit var TextViewTrophy : TextView
    private lateinit var FightButton : Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TextViewTrophy = binding.TextViewTrophy
        TextViewTrophy.text = mainViewModel.getConnectedUser().Trophy.toString()

        val btn = view.findViewById<Button>(R.id.FightButton)
        btn.setOnClickListener {
            mainViewModel.createNewSwap("JU6DqXTpHEbo20TxLcwFdEH3FBO2")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}