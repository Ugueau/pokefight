package com.example.pokefight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

class loginActivity : AppCompatActivity() {

    //élément de ma vue
    private lateinit var connexion : Button
    private lateinit var newUser : Button
    private lateinit var email : TextInputLayout
    private lateinit var password : TextInputLayout

    //activity du tunnel de connexion
    private lateinit var tunnelConnexion : Intent

    //activity principale de l'appli
    private lateinit var mainActivity : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        connexion = this.findViewById(R.id.connexion)
        newUser = this.findViewById(R.id.newUser)
        email = this.findViewById(R.id.InputCreateEmail)
        password = this.findViewById(R.id.InputCreatePassword)

        connexion.setOnClickListener { click -> this.testConnexion() }

        newUser.setOnClickListener { click -> this.tunnelConnexion()}
    }

    fun testConnexion(){
        mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }

    fun tunnelConnexion(){
        tunnelConnexion = Intent(this, TunnelConnexionActivity::class.java)
        startActivity(tunnelConnexion)
        finish()
    }
}