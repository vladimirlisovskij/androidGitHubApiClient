package com.example.githubclient.domain.enteties.apiResponse

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    val login: String,
    val followers: Int,
    val following: Int,
    @SerializedName("avatar_url") val avatarUrl: String,
)