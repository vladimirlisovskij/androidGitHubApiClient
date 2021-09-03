package com.example.githubclient.domain.useCase.sharedPreference

import com.example.githubclient.domain.repository.SharedPreferencesRepository

class LoadUserIdUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend operator fun invoke() = sharedPreferencesRepository.loadUserId()
}