package com.example.githubclient.presentation.enteties

sealed class DialogType {
    data class Error(
        val header: String,
        val message: String
    ) : DialogType()

    object Load : DialogType()

    object NoDialog : DialogType()
}