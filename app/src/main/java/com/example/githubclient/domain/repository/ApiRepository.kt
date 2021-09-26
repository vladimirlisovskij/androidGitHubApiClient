package com.example.githubclient.domain.repository

import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.enteties.apiResponse.UserRepoResponse

interface ApiRepository {
    suspend fun getUserInfo(userId: String): UserInfoResponse

    suspend fun getUserRepositories(userId: String, page: Int): List<UserRepoResponse>
}