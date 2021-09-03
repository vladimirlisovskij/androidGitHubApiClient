package com.example.githubclient.domain.di.includes

import com.example.githubclient.domain.useCase.api.GetUserInfoUseCase
import com.example.githubclient.domain.useCase.sharedPreference.ClearUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.LoadUserIdUseCase
import com.example.githubclient.domain.useCase.sharedPreference.SaveUserIdUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetUserInfoUseCase(get()) }

    factory { LoadUserIdUseCase(get()) }
    factory { SaveUserIdUseCase(get()) }
    factory { ClearUserIdUseCase(get()) }
}