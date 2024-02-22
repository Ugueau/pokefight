package com.example.pokefight.ui.fragmentcreationcompte

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.TunnelConnexionActivity
import com.example.pokefight.ui.ErrorActivity
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FragmentCreationCompte : Fragment() {

    private lateinit var createUser: Button
    private lateinit var InputNickname: TextInputLayout
    private lateinit var InputEmail: TextInputLayout
    private lateinit var InputPassword: TextInputLayout
    private lateinit var InputConfirmPassword: TextInputLayout
    private lateinit var auth: FirebaseAuth

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm: MainViewModel

    companion object {
        fun newInstance() = FragmentCreationCompte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_creation_compte, container, false)

        createUser = view.findViewById(R.id.CreateUser)
        createUser.setOnClickListener { click ->
            MainViewModel.checkNetworkConnection(requireContext()).observe(viewLifecycleOwner){isConnected ->
                if(isConnected){
                    this.createUser()
                }else {
                    val i = Intent(requireContext(), ErrorActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                }
            }
        }

        InputNickname = view.findViewById(R.id.InputCreateNickname)

        InputEmail = view.findViewById(R.id.LayoutEmailCreation)

        InputPassword = view.findViewById(R.id.layoutPasswordCreation)

        InputConfirmPassword = view.findViewById(R.id.InputCreateConfirmPassword)


        return view
    }

    fun createUser(): Boolean {
        var succeed = true
        if (controlChamp()) {
            vm.createUser(
                InputEmail.editText?.text.toString(),
                InputPassword.editText?.text.toString(),
                InputNickname.editText?.text.toString()
            ).observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    val fragmentConfirmCreation = FragmentConfirmCreation()
                    (activity as TunnelConnexionActivity).replaceFragment(
                        fragmentConfirmCreation
                    )
                    Toast.makeText(
                        context,
                        "${user.Email} created",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Account creation failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    succeed = false
                }
            }
        } else {
            succeed = false
        }
        return succeed
    }

    fun controlChamp(): Boolean {

        if (InputEmail.editText?.text.isNullOrEmpty()) {
            InputEmail.error = "Enter an email"
            return false
        }

        if (InputNickname.editText?.text.isNullOrEmpty()) {
            InputNickname.error = "Enter a nickname"
            return false
        }

        if (InputPassword.editText?.text.isNullOrEmpty()) {
            InputPassword.error = "Entrer a password"
            return false
        }

        if (InputConfirmPassword.editText?.text.isNullOrEmpty()) {
            InputConfirmPassword.error = "Confirm your password"
            return false
        }

        if (InputConfirmPassword.editText?.text.toString() != InputPassword.editText?.text.toString()) {
            InputPassword.error = "Different password"
            InputConfirmPassword.error = "Different password"
            return false
        }

        return true
    }

}