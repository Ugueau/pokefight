package com.example.pokefight.ui.swap

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel

class PopupSwapDemand(val swapCreatorName : String) : DialogFragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private var hasClicked = false
    private var hasAccepted = false


    interface OnAcceptedListenner {
        fun onDialogAcceptedSwap()
    }

    private var onAcceptedSListenner: OnAcceptedListenner? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onAcceptedSListenner = context as OnAcceptedListenner
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnAcceptedListenner")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_popup_swap_demand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val creatorDemand = view.findViewById<TextView>(R.id.swapPopup_text)
        creatorDemand.text = "$swapCreatorName ask you a swap"
        val acceptBtn = view.findViewById<Button>(R.id.swapAccept)
        val denyBtn = view.findViewById<Button>(R.id.swapDeny)
        val closeBtn = view.findViewById<Button>(R.id.close_popupSwap)

        acceptBtn.setOnClickListener{
            hasClicked = true
            mainViewModel.sendSwapResponse(true)
            hasAccepted = true
            dismiss()
        }

        denyBtn.setOnClickListener{
            hasClicked = true
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
            dismiss()
        }

        closeBtn.setOnClickListener{
            hasClicked = true
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!hasClicked){
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(hasAccepted){
            onAcceptedSListenner?.onDialogAcceptedSwap()
        }
    }
}