package com.example.pokefight.ui.fight

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.model.RealTimeDatabaseEvent
import com.example.pokefight.ui.MainViewModel

class PopupAskFight(val opponentToken:String): DialogFragment(){

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

        val close = view.findViewById<Button>(R.id.close_popup3);
        close.setOnClickListener {
            dismiss()
        }

        vm = ViewModelProvider(this)[MainViewModel::class.java]

        vm.setNotificationListener { event ->
            when (event) {
                is RealTimeDatabaseEvent.CANCEL_FIGHT -> {
                    if (event.response == true){
                        dismiss()
                    }
                }
                else -> {
                    //Do nothing
                }
            }
        }

    }
}