package com.example.pokefight

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pokefight.ui.ErrorActivity
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    val mainViewModel by viewModels<MainViewModel>()

    //élément de ma vue
    private lateinit var connexion: Button
    private lateinit var newUser: Button
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    //activity du tunnel de connexion
    private lateinit var tunnelConnexion: Intent

    //activity principale de l'appli
    private lateinit var mainActivity: Intent


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

        connexion = this.findViewById(R.id.connexion)
        newUser = this.findViewById(R.id.newUser)
        email = this.findViewById(R.id.InputEmail)
        emailLayout = this.findViewById(R.id.LayoutEmail)
        password = this.findViewById(R.id.InputPassword)
        passwordLayout = this.findViewById(R.id.layoutPassword)

        connexion.setOnClickListener { click ->
            mainViewModel.checkNetworkConnection(this).observe(this) { isConnected ->
                if (isConnected) {
                    this.Connexion()
                } else {
                    val i = Intent(applicationContext, ErrorActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }

        newUser.setOnClickListener { click ->
            mainViewModel.checkNetworkConnection(this).observe(this) { isConnected ->
                if (isConnected) {
                    this.tunnelConnexion()
                } else {
                    val i = Intent(applicationContext, ErrorActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }
    }

    fun Connexion() {

        //gestion de la connexion du User par defaut
        if (email.text.isNullOrEmpty()) {
            //faire en sorte que l'utilisateur voit que l'email est obligatoire
            emailLayout.error = "Mandatory Email"

        } else if (password.text.isNullOrEmpty()) {
            //faire en sorte que l'utilisateur voit que le password est obligatoire
            passwordLayout.error = "Mandatory Password"

        } else {
            mainViewModel.signIn(email.text.toString(), password.text.toString()).observe(this) {
                if (it != null) {
                    Toast.makeText(
                        baseContext,
                        "${it.Email} connected",
                        Toast.LENGTH_SHORT,
                    ).show()

                    mainViewModel.connectUser(it)
                    mainViewModel.endSwapDemand().observe(this) {
                        mainActivity = Intent(this, MainActivity::class.java)
                        startActivity(mainActivity)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    fun tunnelConnexion() {
        tunnelConnexion = Intent(this, TunnelConnexionActivity::class.java)
        startActivity(tunnelConnexion)
        finish()
    }
}