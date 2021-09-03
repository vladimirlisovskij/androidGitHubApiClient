package com.example.githubclient.domain.useCase.sharedPreference

import com.example.githubclient.domain.repository.SharedPreferencesRepository

class SaveUserIdUseCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    suspend operator fun invoke(userId: String) = sharedPreferencesRepository.saveUserId(userId)
}

