package com.example.githubclient.data.reposioty

import com.example.githubclient.data.enteties.apiResponse.DataUserRepoResponse
import com.example.githubclient.data.enteties.apiResponse.toDomainLayer
import com.example.githubclient.data.utils.retrofit.GithubApiService
import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.example.githubclient.domain.enteties.apiResponse.UserRepoResponse
import com.example.githubclient.domain.exceptions.RateLimitException
import com.example.githubclient.domain.exceptions.UserNotFoundException
import com.example.githubclient.domain.repository.ApiRepository

class ApiRepositoryImpl(
    private val githubApiService: GithubApiService
) : ApiRepository {
    override suspend fun getUserInfo(userId: String): UserInfoResponse {
        with(githubApiService.getUserInfo(userId)) {
            if (isSuccessful) {
                return body()!!.toDomainLayer() // not null when isSuccessful
            } else {
                throw getExceptionByCode(code())
            }
        }
    }

    override suspend fun getUserRepositories(userId: String, page: Int): List<UserRepoResponse> {
        with(githubApiService.getUserRepos(userId, page, perPage = 5)) {
            if (isSuccessful) {
                return body()!!.map(DataUserRepoResponse::toDomainLayer)
            } else {
                throw getExceptionByCode(code())
            }
        }
    }

    private fun getExceptionByCode(code: Int) = when (code) {
        404 -> UserNotFoundException()
        403 -> RateLimitException()
        else -> Exception("unknown exception")
    }
}