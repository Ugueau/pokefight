package com.example.pokefight.ui.fragmentcreationcompte

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pokefight.R
import com.example.pokefight.TunnelConnexionActivity
import com.example.pokefight.LoginActivity


class FragmentConfirmCreation : Fragment() {

    private lateinit var buttonContinue : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_confirm_creation, container, false)

        buttonContinue = view.findViewById(R.id.ButtonContinue)
        buttonContinue.setOnClickListener { click ->
            val loginActivity = Intent((activity as TunnelConnexionActivity), LoginActivity::class.java)
            (activity as TunnelConnexionActivity).endTunnelConnexion(loginActivity)
        }
        return view
    }

    companion object {
        fun newInstance() = FragmentCreationCompte()
    }
}