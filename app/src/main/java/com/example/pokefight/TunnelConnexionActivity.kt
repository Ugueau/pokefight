package com.example.pokefight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokefight.ui.fragmentcreationcompte.FragmentCreationCompte

class TunnelConnexionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tunnel_connexion)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentCreationCompte.newInstance())
                .commitNow()
        }
    }
}