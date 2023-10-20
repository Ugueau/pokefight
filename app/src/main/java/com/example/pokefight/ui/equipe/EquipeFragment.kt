package com.example.pokefight.ui.equipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.databinding.FragmentEquipeBinding

class EquipeFragment : Fragment() {

    private var _binding: FragmentEquipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val equipeViewModel =
            ViewModelProvider(this).get(EquipeViewModel::class.java)

        _binding = FragmentEquipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textEquipe
        equipeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}