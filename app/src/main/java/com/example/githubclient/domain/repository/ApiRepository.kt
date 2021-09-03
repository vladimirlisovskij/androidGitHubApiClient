package com.example.githubclient.domain.repository

import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import retrofit2.Response

interface ApiRepository {
    suspend fun getUserInfo(userId: String): Response<UserInfoResponse>
}