package com.example.githubclient.presentation.userProfile

import androidx.lifecycle.viewModelScope
import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.ClearUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val clearUserIdUseCase: ClearUserIdUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val loadUserIdUseCase: LoadUserIdUseCase,
) : BaseViewModel() {
    private val _profileInfo = MutableSharedFlow<UserInfoResponse>()
    val profileInfo = _profileInfo.asSharedFlow()

    fun onScreenCreate() {
        viewModelScope.launch {
            loadUserIdUseCase()?.let { userId ->
                getUserInfoUseCase(userId).also { response ->
                    if (response.isSuccessful) {
                        when (response.code()) {
                            200 -> response.body()?.also {
                                _profileInfo.emit(it)
                            }

                            else -> {

                            }
                        }

                    }
                    // TODO else branch
                }
            }
        }
    }

    fun onLogOutClick() {
        viewModelScope.launch {
            clearUserIdUseCase()
        }
    }
}