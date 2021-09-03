package com.example.githubclient.presentation.utils.dialogs

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.githubclient.R
import com.example.githubclient.databinding.DialogSimpleNotificationBinding


class NotificationDialog: DialogFragment(R.layout.dialog_simple_notification) {
    companion object Fabric{
        const val BACKGROUND_KEY = "BACKGROUND_KEY"
        const val HEADER_KEY = "HEADER_KEY"
        const val TITLE_KEY = "TITLE_KEY"

        fun create(
            backgroundRes: Int,
            headerText: String,
            titleText: String,
        ) = NotificationDialog().apply {
            arguments = Bundle().apply {
                putInt(BACKGROUND_KEY, backgroundRes)
                putString(HEADER_KEY, headerText)
                putString(TITLE_KEY, titleText)
            }
        }
    }

    private val binding: DialogSimpleNotificationBinding by viewBinding()

    @DrawableRes
    private var backgroundRes: Int = 0
    private var headerText: String? = null
    private var titleText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(arguments) {
            backgroundRes = this?.getInt(BACKGROUND_KEY) ?: R.drawable.ic_simple_background
            headerText = this?.getString(HEADER_KEY)
            titleText = this?.getString(TITLE_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvHeader.text = headerText
            tvTitle.text = titleText
            Glide.with(this@NotificationDialog)
                .load(backgroundRes)
                .centerCrop()
                .into(ivHeaderImage)
        }
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}