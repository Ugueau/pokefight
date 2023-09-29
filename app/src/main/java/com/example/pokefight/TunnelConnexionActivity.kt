package com.example.pokefight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pokefight.ui.fragmentcreationcompte.FragmentConfirmCreation
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

    fun replaceFragment(TargetFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TargetFragment)
            .addToBackStack(null)
            .commit()
    }

    fun endTunnelConnexion(TargetActivity: Intent){

        startActivity(TargetActivity)
        finish()
    }
}