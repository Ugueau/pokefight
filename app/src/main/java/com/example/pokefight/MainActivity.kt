package com.example.pokefight

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokefight.databinding.ActivityMainBinding
import com.example.pokefight.model.Pokemon
import com.example.pokefight.model.RealTimeDatabaseEvent
import com.example.pokefight.ui.MainViewModel
import com.example.pokefight.ui.friend.PopupFriendDemand
import com.example.pokefight.ui.swap.PopupSwapDemand
import com.example.pokefight.ui.swap.PopupSwapWaiting
import com.example.pokefight.ui.swap.SwapActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), PopupSwapDemand.OnDialogDestroyListenner {

    private lateinit var binding: ActivityMainBinding
    val mainViewModel by viewModels<MainViewModel>()
    lateinit var vm: MainViewModel
    private var isWaitingForSwapResponse = false
    private var currentPopup: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // valorisation du windowsInsetsController
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // recupération du behavior
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // cacher les barres système
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())



        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_shop,
                R.id.navigation_equipe,
                R.id.navigation_pokedex,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        activeNotifications()

        mainViewModel.logout.observe(this) {
            if (it) {
                val i = Intent(applicationContext, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    fun getPokemons(fromId: Int = 1, toId: Int = fromId + 10, callback: (List<Pokemon>) -> Unit) {
        mainViewModel.getPokemonList(fromId, toId).observe(this) { pokemonList ->
            callback(pokemonList);
        }
    }

    fun getPokemonById(id: Int, callback: (Pokemon) -> Unit) {
        mainViewModel.getPokemonById(id).observe(this) { pokemon ->
            if (pokemon == null) {
                Log.e("PokemonError", "Pokemon not found or unauthorized")
            } else {
                callback(pokemon)
            }
        }
    }

    fun activeNotifications() {
        mainViewModel.setNotificationListener { event ->
            when (event) {
                is RealTimeDatabaseEvent.SWAP_RESPONSE -> {
                    if (event.response) {
                        if (isWaitingForSwapResponse) {
                            (currentPopup as PopupSwapWaiting).stopWaiting(true)
                            currentPopup = null
                            isWaitingForSwapResponse = false
                        }
                        val intent = Intent(this, SwapActivity::class.java)
                        startActivity(intent)
                    } else if (!event.response) {
                        currentPopup?.dismiss()
                        currentPopup = null
                    }
                }

                is RealTimeDatabaseEvent.SWAP_DEMAND -> {
                    if (event.userToken != "") {
                        if (currentPopup == null) {
                            mainViewModel.getNameOf(event.userToken).observe(this) { creatorName ->
                                if (creatorName.isNotEmpty() && currentPopup == null) {
                                    currentPopup = PopupSwapDemand(creatorName)
                                    currentPopup?.show(supportFragmentManager, "popupSwapDemand")
                                }
                            }
                        }
                    }
                }

                is RealTimeDatabaseEvent.SWAP_CREATE_SWAP -> {
                    if (event.targetToken != "" && currentPopup == null) {
                        mainViewModel.createNewSwap(event.targetToken)
                        isWaitingForSwapResponse = true
                        currentPopup = PopupSwapWaiting(event.targetToken)
                        currentPopup?.show(supportFragmentManager, "popupSwapDemand")
                    }
                }

                is RealTimeDatabaseEvent.FRIEND_DEMAND -> {
                    if (event.userToken != "" && currentPopup == null) {
                        if(currentPopup == null){
                            mainViewModel.getNameOf(event.userToken).observe(this) { userName ->
                                val friendPopup = PopupFriendDemand(event.userToken,userName)
                                friendPopup.show(supportFragmentManager, "popupFriendDemand")
                            }
                        }
                    }
                }

                is RealTimeDatabaseEvent.FRIEND_RESPONSE -> {
                    if (event.userToken != "") {
                        mainViewModel.addFriend(event.userToken)
                    }
                    else{
                        //Notify user refused friend request
                    }
                }

                else -> {
                    //Do nothing
                }
            }
        }
    }

    override fun onDialogAcceptedSwap() {
        val intent = Intent(this, SwapActivity::class.java)
        startActivity(intent)
    }

    override fun onDialogDismiss() {
        currentPopup = null
    }
}