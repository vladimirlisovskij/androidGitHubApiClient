package com.example.githubclient.data.utils.retrofit

import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {
    @GET("/users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: String): Response<UserInfoResponse>

    @GET("/users/{userId}/repos")
    suspend fun getUserRepos(@Path("userId") userId: String)
}