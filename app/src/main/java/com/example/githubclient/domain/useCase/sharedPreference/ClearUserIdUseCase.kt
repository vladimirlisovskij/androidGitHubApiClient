package com.example.githubclient.domain.useCase.sharedPreference

import com.example.githubclient.domain.repository.SharedPreferencesRepository

class ClearUserIdUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend operator fun invoke() = sharedPreferencesRepository.clearUserId()
}