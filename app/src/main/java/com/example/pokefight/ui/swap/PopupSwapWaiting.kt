package com.example.pokefight.ui.swap

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

class PopupSwapWaiting(private val swaperToken: String) : DialogFragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private var hasClicked = false
    private var response = false
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

        super.onViewCreated(view, savedInstanceState)
        val creatorDemand = view.findViewById<TextView>(R.id.swapPopup_text)
        val acceptBtn = view.findViewById<Button>(R.id.swapAccept)
        val denyBtn = view.findViewById<Button>(R.id.swapDeny)
        val closeBtn = view.findViewById<Button>(R.id.close_popupSwap)

        mainViewModel.getNameOf(swaperToken).observe(this) { name ->
            acceptBtn.isEnabled = false
            acceptBtn.visibility = View.INVISIBLE
            creatorDemand.text = "Waiting for $name"
        }

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

    fun stopWaiting(response: Boolean) {
        this.response = response
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!hasClicked && !response) {
            mainViewModel.sendSwapResponse(false)
            mainViewModel.endSwapDemand()
        }
    }
}