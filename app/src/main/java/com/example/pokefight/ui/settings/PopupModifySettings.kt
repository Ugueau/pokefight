package com.example.pokefight.ui.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.pokefight.R
import com.example.pokefight.ui.MainViewModel

class PopupModifySettings : DialogFragment() {
    val mainViewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_popup_modify_settings, container, false)
        dialog?.let { dialog ->
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                requestFeature(Window.FEATURE_NO_TITLE)
            }
        }
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val close = view.findViewById<Button>(R.id.close_popup);
        close.setOnClickListener {
            dismiss()
        }

        val user = mainViewModel.getConnectedUser()
        val nickname = view.findViewById<EditText>(R.id.modify_nickname)
        nickname.setText(user.Nickname)

        val confirm = view.findViewById<Button>(R.id.modify_confirm)
        confirm.setOnClickListener {
            user.Nickname = nickname.text.toString()
            mainViewModel.updateUser(user)
            dismiss()
        }
    }
}