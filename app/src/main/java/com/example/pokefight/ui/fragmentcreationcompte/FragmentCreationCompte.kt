package com.example.pokefight.ui.fragmentcreationcompte

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pokefight.R
import com.example.pokefight.TunnelConnexionActivity

class FragmentCreationCompte : Fragment() {

    private lateinit var createUser : Button


    companion object {
        fun newInstance() = FragmentCreationCompte()
    }

    private lateinit var viewModel: FragmentCreationCompteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(FragmentCreationCompteViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_creation_compte, container, false)

        createUser = view.findViewById(R.id.CreateUser)
        createUser.setOnClickListener { click -> this.createUser() }

        return view
    }

    fun createUser(){

        Log.e("TEST", "test", )

        (activity as TunnelConnexionActivity).replaceFragmentConfirmCreation()

    }

}