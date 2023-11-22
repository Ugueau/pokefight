package com.example.pokefight.ui.fragmentcreationcompte

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.TunnelConnexionActivity
import com.example.pokefight.VIewModel.UserViewModel
import com.example.pokefight.model.User
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.textfield.TextInputLayout

class FragmentCreationCompte : Fragment() {

    private lateinit var createUser : Button
    private lateinit var InputNickname: TextInputLayout
    private lateinit var InputEmail: TextInputLayout
    private lateinit var InputPassword: TextInputLayout
    private lateinit var InputConfirmPassword: TextInputLayout
    private lateinit var user : User

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm : MainViewModel

    companion object {
        fun newInstance() = FragmentCreationCompte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_creation_compte, container, false)

        createUser = view.findViewById(R.id.CreateUser)
        createUser.setOnClickListener { click -> this.createUser() }

        InputNickname = view.findViewById(R.id.InputCreateNickname)

        InputEmail = view.findViewById(R.id.LayoutEmailCreation)

        InputPassword = view.findViewById(R.id.layoutPasswordCreation)

        InputConfirmPassword = view.findViewById(R.id.InputCreateConfirmPassword)


        return view
    }

    fun createUser(){

        user = User(
            InputEmail.editText?.text.toString(),
            InputPassword.editText?.text.toString(),
            InputNickname.editText?.text.toString(),
            0,
            "1234567890",
        )

        Log.e("user_créé", user.toString())

        MainViewModel.createUser(user)

        val fragmentConfirmCreation = FragmentConfirmCreation()
        (activity as TunnelConnexionActivity).replaceFragment(fragmentConfirmCreation)



    }

}