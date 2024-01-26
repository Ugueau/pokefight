package com.example.pokefight.ui.swap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.pokefight.R
import com.example.pokefight.domain.SwapRepository
import com.example.pokefight.domain.UserRepository
import com.example.pokefight.ui.MainViewModel

class PopupSwapDemand(val swapCreatorName : String) : DialogFragment() {

    val mainViewModel by activityViewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_popup_swap_demand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val creatorDemand = view.findViewById<TextView>(R.id.swapPopup_creatorName)
        creatorDemand.text = "$swapCreatorName ask you a swap"
        val acceptBtn = view.findViewById<Button>(R.id.swapAccept)
        val denyBtn = view.findViewById<Button>(R.id.swapDeny)
        val closeBtn = view.findViewById<Button>(R.id.close_popupSwap)

        acceptBtn.setOnClickListener{
            mainViewModel.sendSwapResponse(true)
            dismiss()
        }

        denyBtn.setOnClickListener{
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwap()
            dismiss()
        }

        closeBtn.setOnClickListener{
            dismiss()
        }
    }

}