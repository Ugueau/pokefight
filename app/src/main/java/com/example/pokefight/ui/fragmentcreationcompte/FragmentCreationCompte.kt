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
import kotlin.random.Random

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

        if (controlChamp()){
            var userToConnect = User(
                InputEmail.editText?.text.toString(),
                InputPassword.editText?.text.toString(),
                InputNickname.editText?.text.toString(),
                0,
                0,
                Random.nextInt(1, 99999999).toString(),
                null
            )

            vm.insertUser(userToConnect).observe(viewLifecycleOwner){
                if (it){
                    val fragmentConfirmCreation = FragmentConfirmCreation()
                    (activity as TunnelConnexionActivity).replaceFragment(fragmentConfirmCreation)
                }
                else{
                    InputEmail.error = "User allready existe"
                }
            }
        }
    }

    fun controlChamp(): Boolean{

        if(InputEmail.editText?.text.isNullOrEmpty()){
            InputEmail.error = "Enter an email"
            return false
        }

        if(InputNickname.editText?.text.isNullOrEmpty()){
            InputNickname.error = "Enter a nickname"
            return false
        }

        if(InputPassword.editText?.text.isNullOrEmpty()){
            InputPassword.error = "Entrer a password"
            return false
        }

        if(InputConfirmPassword.editText?.text.isNullOrEmpty()){
            InputConfirmPassword.error = "Confirm your password"
            return false
        }

        if (InputConfirmPassword.editText?.text.toString() != InputPassword.editText?.text.toString()){
            InputPassword.error = "Different password"
            InputConfirmPassword.error = "Different password"
            return false
        }

        return true
    }

}