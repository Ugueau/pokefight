package com.example.pokefight.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import com.example.pokefight.LoginActivity
import com.example.pokefight.R
import com.example.pokefight.domain.api.ConnectionManager
import com.example.pokefight.ui.ErrorActivity
import com.example.pokefight.ui.MainViewModel
import com.example.pokefight.ui.pokedex.PopupPokemonDetail

class SettingsFragment : Fragment() {

    val mainViewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.checkNetworkConnection(requireContext()).observe(viewLifecycleOwner){isConnected ->
            if(!isConnected){
                val i = Intent(requireContext(), ErrorActivity::class.java)
                startActivity(i)
                activity?.finish()
            }
        }

        var user = mainViewModel.getConnectedUser()

        val nickname = view.findViewById<TextView>(R.id.settings_nickname)
        nickname.text = user.Nickname
        val email = view.findViewById<TextView>(R.id.settings_email)
        email.text = user.Email
        val modifyButton = view.findViewById<Button>(R.id.settings_modify)
        modifyButton.setOnClickListener {
            mainViewModel.checkNetworkConnection(requireContext()).observe(viewLifecycleOwner){isConnected ->
                if(isConnected){
                    val popupModifySettings = PopupModifySettings()
                    popupModifySettings.show(
                        (activity as AppCompatActivity).supportFragmentManager,
                        "popupModifySettings"
                    )
                }else {
                    val i = Intent(requireContext(), ErrorActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                }
            }
        }
        val logoutBtn = view.findViewById<Button>(R.id.settings_logout)
        logoutBtn.setOnClickListener{
            mainViewModel.checkNetworkConnection(requireContext()).observe(viewLifecycleOwner){isConnected ->
                if(isConnected){
                    mainViewModel.logout()
                }else {
                    val i = Intent(requireContext(), ErrorActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                }
            }
        }

        mainViewModel.userUpdated.observe(viewLifecycleOwner){
            user = mainViewModel.getConnectedUser()
            nickname.text = user.Nickname
            email.text = user.Email
        }
    }


}