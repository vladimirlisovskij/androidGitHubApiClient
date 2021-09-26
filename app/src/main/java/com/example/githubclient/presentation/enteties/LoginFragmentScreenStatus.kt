package com.example.githubclient.presentation.enteties

sealed class LoginFragmentScreenStatus {
    data class Error(
        val header: String,
        val message: String,
    ) : LoginFragmentScreenStatus()

    object OpenProfile : LoginFragmentScreenStatus()

    object Load: LoginFragmentScreenStatus()

    object ClearState: LoginFragmentScreenStatus()
}