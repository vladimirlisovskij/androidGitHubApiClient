package com.example.githubclient.presentation.loginFragment

import androidx.lifecycle.viewModelScope
import com.example.githubclient.domain.exceptions.RateLimitException
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.SaveUserIdUseCase
import com.example.githubclient.presentation.enteties.LoginFragmentScreenStatus
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class LoginViewModel(
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel() {
    private val _currentScreen = MutableStateFlow<LoginFragmentScreenStatus>(LoginFragmentScreenStatus.ClearState)
    val currentScreens = _currentScreen.asStateFlow()

    fun onDialogOkClick() {
        viewModelScope.launch {
            _currentScreen.emit(LoginFragmentScreenStatus.ClearState)
        }
    }

    fun onLoginClick(userId: String) {
        viewModelScope.launch {
            try {
                _currentScreen.emit(LoginFragmentScreenStatus.Load)
                saveUserIdUseCase(getUserInfoUseCase(userId).login)
                _currentScreen.emit(LoginFragmentScreenStatus.OpenProfile) // can throw exception
            } catch (e: UserNotFoundException) {
                _currentScreen.emit(
                    LoginFragmentScreenStatus.Error(
                        "Authorization failed",
                        "Profile not found"
                    )
                )
            } catch (e: RateLimitException) {
                LoginFragmentScreenStatus.Error(
                    "Rate limit error",
                    "Rate limit exceeded"
                )
            } catch (e: UnknownHostException) {
                _currentScreen.emit(
                    LoginFragmentScreenStatus.Error(
                        "Network error",
                        "Check your network connection"
                    )
                )
            } catch (e: Exception) {
                _currentScreen.emit(
                    LoginFragmentScreenStatus.Error(
                        "Unknown exception",
                        ""
                    )
                )
            }
        }
    }
}