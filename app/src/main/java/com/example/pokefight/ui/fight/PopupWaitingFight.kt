package com.example.pokefight.ui.fight

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel

class PopupWaitingFight(val opponentToken:String) : DialogFragment(){

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }

        return inflater.inflate(R.layout.fragment_popup_waiting_fight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popupFight);
        close.setOnClickListener {
            vm.cancelFight(opponentToken)
            dismiss()
        }

        vm = ViewModelProvider(this)[MainViewModel::class.java]

        val askPopup_text = view.findViewById<TextView>(R.id.askPopup_text)

        vm.getUser(opponentToken).observe(viewLifecycleOwner){
            if (it != null){
                askPopup_text.text = "Waiting for ${it.Nickname}"
            }
        }
    }
}