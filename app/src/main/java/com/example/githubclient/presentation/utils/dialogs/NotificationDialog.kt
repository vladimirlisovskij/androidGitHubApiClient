package com.example.githubclient.presentation.utils.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.githubclient.R
import com.example.githubclient.databinding.DialogSimpleNotificationBinding


class NotificationDialog: DialogFragment(R.layout.dialog_simple_notification) {
    companion object {
        fun create() = NotificationDialog()

        const val TAG = "NotificationDialog.TAG"
    }

    private val binding: DialogSimpleNotificationBinding by viewBinding()

    var headerText: String? = null
    var messageText: String? = null

    var onClickListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvHeader.text = headerText
            tvTitle.text = messageText
            btnOk.setOnClickListener {
                onClickListener?.invoke()
            }
        }
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}