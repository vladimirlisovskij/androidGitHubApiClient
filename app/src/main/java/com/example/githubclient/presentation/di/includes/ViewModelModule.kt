package com.example.githubclient.presentation.di.includes

import com.example.githubclient.presentation.loginFragment.LoginViewModel
import com.example.githubclient.presentation.mainActivity.MainActivityViewModel
import com.example.githubclient.presentation.repositoryList.RepositoryListViewModel
import com.example.githubclient.presentation.userProfile.UserProfileViewModel
import com.example.githubclient.presentation.utils.sharedViewModels.MainNavigatorViewModel
import com.example.githubclient.presentation.utils.sharedViewModels.UserInfoNavigationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainActivityViewModel()
    }

    viewModel {
        MainNavigatorViewModel(
            loadUserIdUseCase = get()
        )
    }

    viewModel {
        UserInfoNavigationViewModel()
    }

    viewModel {
        LoginViewModel(
            saveUserIdUseCase = get(),
            getUserInfoUseCase = get()
        )
    }

    viewModel {
        RepositoryListViewModel(
            getUserIdUseCase = get(),
            getUserRepositories = get(),
            clearUserIdUseCase = get()
        )
    }

    viewModel {
        UserProfileViewModel(
            getUserInfoUseCase = get(),
            loadUserIdUseCase = get(),
            clearUserIdUseCase = get(),
        )
    }
}

