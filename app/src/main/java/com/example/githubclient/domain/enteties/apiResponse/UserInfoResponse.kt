package com.example.githubclient.domain.enteties.apiResponse

data class UserInfoResponse(
    val login: String,
    val followers: Int,
    val following: Int,
    val publicRepos: Int,
    val avatarUrl: String,
    val profileUrl: String,
)
