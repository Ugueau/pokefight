package com.example.pokefight.ui.friend

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

class PopupFriendDemand(private val userToken: String, private val userName : String) : DialogFragment() {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private var hasClicked = false

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
            mainViewModel.sendFriendResponse(userToken,true)
            mainViewModel.addFriend(userToken)
            dismiss()
        }

        creatorDemand.text = "$userName asks you as a friend"


        denyBtn.setOnClickListener {
            hasClicked = true
            mainViewModel.sendFriendResponse(userToken,false)
            dismiss()
        }

        closeBtn.setOnClickListener {
            hasClicked = true
            mainViewModel.sendFriendResponse(userToken,false)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!hasClicked) {
            mainViewModel.sendFriendResponse(userToken,false)
        }
    }
}