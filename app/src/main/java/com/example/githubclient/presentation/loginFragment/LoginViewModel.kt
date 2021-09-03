package com.example.githubclient.presentation.loginFragment

import androidx.lifecycle.viewModelScope
import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.SaveUserIdUseCase
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

sealed class LoginFragmentScreenStatus {
    data class Error(val message: String) : LoginFragmentScreenStatus()
    object OpenProfile : LoginFragmentScreenStatus()
}

class LoginViewModel(
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel() {
    private val _currentScreen = MutableSharedFlow<LoginFragmentScreenStatus>()
    val currentScreens = _currentScreen.asSharedFlow()

    fun onLoginClick(userId: String) {
        viewModelScope.launch {
            try {
                with(getUserInfoUseCase(userId)) {
                    if (isSuccessful && code() == 200) {
                        saveUserIdUseCase(userId)
                        _currentScreen.emit(LoginFragmentScreenStatus.OpenProfile)
                    } else {
                        _currentScreen.emit(LoginFragmentScreenStatus.Error("Profile not found"))
                    }
                }
            } catch (e: UnknownHostException) {
                _currentScreen.emit(LoginFragmentScreenStatus.Error("Check your network connection"))
            }
        }
    }


}