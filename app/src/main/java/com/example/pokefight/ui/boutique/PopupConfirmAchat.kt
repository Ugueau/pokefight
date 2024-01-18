package com.example.pokefight.ui.boutique

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel

class PopupConfirmAchat(val key : String, val callback : () -> Unit) : DialogFragment() {

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm : MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }

        return inflater.inflate(R.layout.fragment_popup_confirm_achat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popup3);
        close.setOnClickListener {
            callback()
            dismiss()
        }

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        var layoutItemAchete : ConstraintLayout = view.findViewById(R.id.layoutItemAchete)

        when (key){
            "COMMON" ->

            "UNCOMMON" ->

            "RARE" ->

            "POKEBALL" ->

            "SUPERBALL" ->

            "HYPERBALL" ->

        }
    }

}