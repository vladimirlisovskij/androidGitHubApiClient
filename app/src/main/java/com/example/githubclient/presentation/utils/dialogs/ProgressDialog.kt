package com.example.githubclient.presentation.utils.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.githubclient.R

class ProgressDialog: DialogFragment(R.layout.dialog_progress) {
    companion object {
        fun create() = ProgressDialog()

        const val TAG = "ProgressDialog.TAG"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}