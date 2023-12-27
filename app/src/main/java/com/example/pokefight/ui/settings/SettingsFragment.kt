package com.example.pokefight.ui.settings

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
import com.example.pokefight.R
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

        var user = mainViewModel.getConnectedUser()

        val nickname = view.findViewById<TextView>(R.id.settings_nickname)
        nickname.text = user.Nickname
        val email = view.findViewById<TextView>(R.id.settings_email)
        email.text = user.Email
        val modifyButton = view.findViewById<Button>(R.id.settings_modify)
        modifyButton.setOnClickListener {
            val popupModifySettings = PopupModifySettings()
            popupModifySettings.show((activity as AppCompatActivity).supportFragmentManager, "popupModifySettings")
        }

        mainViewModel.userUpdated.observe(viewLifecycleOwner){
            user = mainViewModel.getConnectedUser()
            nickname.text = user.Nickname
            email.text = user.Email
        }
    }


}