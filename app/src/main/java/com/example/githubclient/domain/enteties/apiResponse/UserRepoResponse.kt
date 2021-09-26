package com.example.githubclient.domain.enteties.apiResponse

data class UserRepoResponse(
    val name: String,
    val created: String,
    val updated: String,
    val pushed: String,
    val language: String?,
    val url: String
)