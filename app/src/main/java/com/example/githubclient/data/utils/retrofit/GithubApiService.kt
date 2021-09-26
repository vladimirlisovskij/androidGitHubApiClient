package com.example.githubclient.data.utils.retrofit

import com.example.githubclient.data.enteties.apiResponse.DataUserInfoResponse
import com.example.githubclient.data.enteties.apiResponse.DataUserRepoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("/users/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: String): Response<DataUserInfoResponse>

    @GET("/users/{userId}/repos")
    suspend fun getUserRepos(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<DataUserRepoResponse>>
}