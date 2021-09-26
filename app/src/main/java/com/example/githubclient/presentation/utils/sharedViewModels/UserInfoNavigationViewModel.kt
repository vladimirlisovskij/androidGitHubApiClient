package com.example.githubclient.presentation.utils.sharedViewModels

import com.example.githubclient.presentation.enteties.UserInfoScreens
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

class UserInfoNavigationViewModel : BaseViewModel() {
    private val _currentScreen = MutableStateFlow(UserInfoScreens.USER_PROFILE)
    val currentScreen= _currentScreen.asStateFlow()

    fun navigateToRepositoryList() {
        runBlocking {
            _currentScreen.emit(UserInfoScreens.REPOSITORY_LIST)
        }
    }

    fun navigateToProfileInfo() {
        runBlocking {
            _currentScreen.emit(UserInfoScreens.USER_PROFILE)
        }
    }
}

