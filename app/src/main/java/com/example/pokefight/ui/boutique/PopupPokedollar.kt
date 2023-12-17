package com.example.pokefight.ui.boutique

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.example.pokefight.R
class PopupPokedollar : DialogFragment() {

    private lateinit var offre1 : LinearLayout;
    private lateinit var offre2 : LinearLayout;

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

        offre1 = v.findViewById(R.id.offre1);
        offre1.setOnClickListener{

        }

        offre2 = v.findViewById(R.id.offre2);

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popup2);
        close.setOnClickListener {
            dismiss()
        }

    }
}