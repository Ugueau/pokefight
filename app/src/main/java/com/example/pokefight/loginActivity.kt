package com.example.pokefight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.VIewModel.UserViewModel
import com.example.pokefight.model.User
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class loginActivity : AppCompatActivity() {

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm : MainViewModel

    //élément de ma vue
    private lateinit var connexion : Button
    private lateinit var newUser : Button
    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText

    private lateinit var emailLayout : TextInputLayout
    private lateinit var passwordLayout : TextInputLayout

    //activity du tunnel de connexion
    private lateinit var tunnelConnexion : Intent

    //activity principale de l'appli
    private lateinit var mainActivity : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // valorisation du windowsInsetsController
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // recupération du behavior
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // cacher les barres système
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        connexion = this.findViewById(R.id.connexion)
        newUser = this.findViewById(R.id.newUser)
        email = this.findViewById(R.id.InputEmail)
        emailLayout = this.findViewById(R.id.LayoutEmail)
        password = this.findViewById(R.id.InputPassword)
        passwordLayout = this.findViewById(R.id.layoutPassword)

        connexion.setOnClickListener { click -> this.Connexion() }

        newUser.setOnClickListener { click -> this.tunnelConnexion()}
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun Connexion(){

        //gestion de la connexion du User par defaut
        if (email.text.isNullOrEmpty()){

            //faire en sorte que l'utilisateur voit que l'email est obligatoire
            emailLayout.setError("Mandatory Email")

        }
        else if(password.text.isNullOrEmpty()){
            //faire en sorte que l'utilisateur voit que le password est obligatoire
            email.error = null
            passwordLayout.setError("Mandatory Password")

        }
        else{
            if (MainViewModel.fetchUser(email.text.toString(), password.text.toString())){
                mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
                finish()
            }
        }
    }

    fun tunnelConnexion(){
        tunnelConnexion = Intent(this, TunnelConnexionActivity::class.java)
        startActivity(tunnelConnexion)
        finish()
    }
}