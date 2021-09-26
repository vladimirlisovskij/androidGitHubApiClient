package com.example.githubclient.presentation.userProfile

import androidx.lifecycle.viewModelScope
import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.exceptions.RateLimitException
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.ClearUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserProfileScreenState {
    data class Error(val header: String, val message: String) : UserProfileScreenState()
    object Default : UserProfileScreenState()
    object NavigateToLogin : UserProfileScreenState()
    object Progress : UserProfileScreenState()
}

class UserProfileViewModel(
    private val clearUserIdUseCase: ClearUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val loadUserIdUseCase: LoadUserIdUseCase,
) : BaseViewModel() {
    private val _profileInfo = MutableSharedFlow<UserInfoResponse>()
    val profileInfo = _profileInfo.asSharedFlow()

    private val _screenState =
        MutableStateFlow<UserProfileScreenState>(UserProfileScreenState.Default)
    val screenState = _screenState.asStateFlow()

    private val _profileUrl = MutableSharedFlow<String>()
    val profileUrl = _profileUrl.asSharedFlow()

    fun onErrorDialogOkClick() {
        viewModelScope.launch {
            clearUserIdUseCase()
            _screenState.emit(UserProfileScreenState.NavigateToLogin)
        }
    }

    fun onOpenProfile(url: String) {
        viewModelScope.launch {
            _profileUrl.emit(url)
        }
    }

    fun onScreenCreate() {
        viewModelScope.launch {
            loadUserIdUseCase()?.let { userId ->
                try {
                    _screenState.emit(UserProfileScreenState.Progress)
                    getUserInfoUseCase(userId).also {
                        _profileInfo.emit(it)
                    }
                    _screenState.emit(UserProfileScreenState.Default)
                } catch (e: UserNotFoundException) {
                    _screenState.emit(
                        UserProfileScreenState.Error(
                            "User error",
                            "Invalid user id"
                        )
                    )
                } catch (e: RateLimitException) {
                    _screenState.emit(
                        UserProfileScreenState.Error(
                            "Rate limit error",
                            "Rate limit exceeded"
                        )
                    )
                } catch (e: Exception) {
                    _screenState.emit(
                        UserProfileScreenState.Error(
                            "Unknown exception",
                            ""
                        )
                    )
                }
            }
        }
    }

    fun onLogOutClick() {
        viewModelScope.launch {
            clearUserIdUseCase()
            _screenState.emit(UserProfileScreenState.NavigateToLogin)
        }
    }
}