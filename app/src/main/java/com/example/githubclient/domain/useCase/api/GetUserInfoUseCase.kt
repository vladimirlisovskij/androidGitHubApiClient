package com.example.githubclient.domain.useCase.api

import com.example.githubclient.domain.repository.ApiRepository

class GetUserInfoUseCase(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(userId: String) = apiRepository.getUserInfo(userId)
}