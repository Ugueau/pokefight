package com.example.pokefight

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
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

        // valorisation du windowsInsetsController
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // recupération du behavior
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // cacher les barres système
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

    }

    fun replaceFragment(TargetFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TargetFragment)
            .addToBackStack(null)
            .commit()
    }

    fun endTunnelConnexion(TargetActivity: Intent) {

        startActivity(TargetActivity)
        finish()
    }
}