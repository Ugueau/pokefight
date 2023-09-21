package com.example.pokefight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class loginActivity : AppCompatActivity() {

    //élément de ma vue
    private lateinit var connexion : Button
    private lateinit var newUser : Button
    private lateinit var email : EditText
    private lateinit var password : EditText

    //activity du tunnel de connexion
    private lateinit var tunnelConnexion : Intent

    //activity principale de l'appli
    private lateinit var mainActivity : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        connexion = this.findViewById(R.id.connexion)
        newUser = this.findViewById(R.id.newUser)
        email = this.findViewById(R.id.email)
        password = this.findViewById(R.id.password)

        connexion.setOnClickListener { click -> this.testConnexion() }
    }

    fun testConnexion(){
        mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
    }

    fun tunnelConnexion(){
        tunnelConnexion = Intent(this, TunnelConnexionActivity::class.java)
        startActivity(tunnelConnexion)
    }
}