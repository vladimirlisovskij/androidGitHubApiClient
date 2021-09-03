package com.example.githubclient.data.reposioty

import com.example.githubclient.data.utils.retrofit.GithubApiService
import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.repository.ApiRepository
import retrofit2.Response

class ApiRepositoryImpl(
    private val githubApiService: GithubApiService
) : ApiRepository {
    override suspend fun getUserInfo(userId: String): Response<UserInfoResponse> {
        return githubApiService.getUserInfo(userId)
    }
}