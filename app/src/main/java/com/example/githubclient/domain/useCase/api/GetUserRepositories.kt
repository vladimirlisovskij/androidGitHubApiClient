package com.example.githubclient.domain.useCase.api

import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.repository.ApiRepository

class GetUserRepositories(
    private val apiRepository: ApiRepository
) {
    suspend operator fun invoke(userId: String, page: Int) = apiRepository.getUserRepositories(userId, page)
}