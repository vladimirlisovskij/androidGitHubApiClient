package com.example.githubclient.presentation.repositoryList

import androidx.lifecycle.viewModelScope
import com.example.githubclient.domain.enteties.apiResponse.UserRepoResponse
import com.example.githubclient.domain.exceptions.RateLimitException
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.useCase.api.GetUserRepositories
import com.example.githubclient.domain.useCase.sharedPreference.ClearUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.presentation.userProfile.UserProfileScreenState
import com.example.githubclient.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RepositoryListScreenState {
    data class Error(val header: String, val message: String) : RepositoryListScreenState()
    object Default : RepositoryListScreenState()
    object NavigateToLogin : RepositoryListScreenState()
}

class RepositoryListViewModel(
    private val clearUserIdUseCase: ClearUserIdUseCase,
    private val getUserRepositories: GetUserRepositories,
    private val getUserIdUseCase: LoadUserIdUseCase
) : BaseViewModel() {
    private var currentPage = 1

    private val _screenState =
        MutableStateFlow<RepositoryListScreenState>(RepositoryListScreenState.Default)
    val screenState = _screenState.asStateFlow()

    private val _repositories = MutableStateFlow<List<UserRepoResponse>>(listOf())
    val repositories = _repositories.asStateFlow()

    private val _isLoad = MutableStateFlow(true)
    val isLoad = _isLoad.asStateFlow()

    private val _repoUrl = MutableSharedFlow<String>()
    val repoUrl = _repoUrl.asSharedFlow()

    fun onOpenRepoClick(url: String) {
        viewModelScope.launch {
            _repoUrl.emit(url)
        }
    }

    fun onDialogClick() {
        viewModelScope.launch {
            clearUserIdUseCase()
            _screenState.emit(RepositoryListScreenState.NavigateToLogin)
        }
    }

    fun requestItems() {
        viewModelScope.launch {
            try {
                getUserIdUseCase()?.also {
                    with(getUserRepositories(it, currentPage)) {
                        _repositories.emit(this)
                        if (isEmpty()) _isLoad.emit(false)
                    }
                    currentPage++
                }
            } catch (e: UserNotFoundException) {
                _screenState.emit(
                    RepositoryListScreenState.Error(
                        "User error",
                        "Invalid user id"
                    )
                )
            } catch (e: RateLimitException) {
                _screenState.emit(
                    RepositoryListScreenState.Error(
                        "Rate limit error",
                        "Rate limit exceeded"
                    )
                )
            } catch (e: Exception) {
                _screenState.emit(
                    RepositoryListScreenState.Error(
                        "Unknown exception",
                        ""
                    )
                )
            }
        }
    }
}