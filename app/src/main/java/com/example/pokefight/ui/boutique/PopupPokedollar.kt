package com.example.pokefight.ui.boutique

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class PopupPokedollar(val callback : () -> Unit) : DialogFragment() {

    val MainViewModel by viewModels<MainViewModel>()
    lateinit var vm : MainViewModel

    private lateinit var offre1 : LinearLayout
    private lateinit var offre2 : LinearLayout

    private lateinit var InputCode : TextInputEditText
    private lateinit var LayoutInputCode : TextInputLayout
    private lateinit var valideCode : Button

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_popup_pokedollar, container, false)
        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        //gestion de l'offre a + 500 pokedollar
        offre1 = v.findViewById(R.id.offre1)
        offre1.setOnClickListener{
            vm.updateUserSolde(500)
            callback()
            dismiss()
        }

        //gestion de l'offre a + 1000 pokedollar
        offre2 = v.findViewById(R.id.offre2)
        offre2.setOnClickListener{
            vm.updateUserSolde(1000)
            callback()
            dismiss()
        }

        //gestion de la zone pour entrer un code
        LayoutInputCode = v.findViewById(R.id.LayoutInputCode)
        InputCode = v.findViewById(R.id.InputCode)
        valideCode = v.findViewById(R.id.valideCode)

        valideCode.setOnClickListener {
            if (!InputCode.text.toString().isNullOrEmpty()){
                if (InputCode.text.toString().uppercase(Locale.ROOT) == "STONKS"){
                    vm.updateUserSolde(10000)
                    callback()
                    dismiss()
                }
                else{
                    LayoutInputCode.error = "Invalid code"
                }
            }
            else{
                LayoutInputCode.error = "Mandatory code"
            }
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popup2);
        close.setOnClickListener {
            callback()
            dismiss()
        }

    }
}