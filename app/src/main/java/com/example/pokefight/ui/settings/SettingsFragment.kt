package com.example.pokefight.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel

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

        val user = mainViewModel.getConnectedUser()

        val nickname = view.findViewById<TextView>(R.id.settings_nickname)
        nickname.text = user.Nickname
        val email = view.findViewById<TextView>(R.id.settings_email)
        email.text = user.Email
    }


}