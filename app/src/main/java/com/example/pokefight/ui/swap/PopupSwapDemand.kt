package com.example.pokefight.ui.swap

import android.content.Context
import android.content.DialogInterface
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
import androidx.fragment.app.activityViewModels
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel

class PopupSwapDemand(private val swaperName: String) : DialogFragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private var hasClicked = false
    private var hasAccepted = false


    interface OnDialogDestroyListenner {
        fun onDialogAcceptedSwap()
        fun onDialogDismiss()
    }

    private var onDestroyListenner: OnDialogDestroyListenner? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDestroyListenner = context as OnDialogDestroyListenner
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnAcceptedListenner")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return inflater.inflate(R.layout.fragment_popup_swap_demand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val creatorDemand = view.findViewById<TextView>(R.id.swapPopup_text)
        val acceptBtn = view.findViewById<Button>(R.id.swapAccept)
        val denyBtn = view.findViewById<Button>(R.id.swapDeny)
        val closeBtn = view.findViewById<Button>(R.id.close_popupSwap)

        acceptBtn.setOnClickListener {
            hasClicked = true
            mainViewModel.sendSwapResponse(true)
            hasAccepted = true
            dismiss()
        }

        creatorDemand.text = "$swaperName ask you a swap"


        denyBtn.setOnClickListener {
            hasClicked = true
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
            dismiss()
        }

        closeBtn.setOnClickListener {
            hasClicked = true
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!hasClicked) {
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDestroyListenner?.onDialogDismiss()
        if (hasAccepted) {
            onDestroyListenner?.onDialogAcceptedSwap()
        }
    }
}