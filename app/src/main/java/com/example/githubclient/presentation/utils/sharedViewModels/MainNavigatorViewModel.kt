package com.example.githubclient.presentation.utils.sharedViewModels

import androidx.lifecycle.viewModelScope
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.presentation.enteties.LoginStatusScreens
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainNavigatorViewModel(
    private val loadUserIdUseCase: LoadUserIdUseCase
) : BaseViewModel() {
    private val _currentScreen =  MutableSharedFlow<LoginStatusScreens>()
    val currentScreens = _currentScreen.asSharedFlow()

    fun onScreenCreate() {
        viewModelScope.launch {
            _currentScreen.emit(if (loadUserIdUseCase() == null) LoginStatusScreens.LOGIN else LoginStatusScreens.USER_INFO)
        }
    }

    fun openLoginScreen() {
        viewModelScope.launch {
            _currentScreen.emit(LoginStatusScreens.LOGIN)
        }
    }

    fun openUserInfoScreen() {
        viewModelScope.launch {
            _currentScreen.emit(LoginStatusScreens.USER_INFO)
        }
    }
}