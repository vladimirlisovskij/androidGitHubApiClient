package com.example.githubclient.data.enteties.apiResponse

import com.example.githubclient.domain.enteties.apiResponse.UserInfoResponse
import com.google.gson.annotations.SerializedName

data class DataUserInfoResponse(
    val login: String,
    val followers: Int,
    val following: Int,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val profileUrl: String,
)

fun DataUserInfoResponse.toDomainLayer() = UserInfoResponse(
    login = login,
    followers = followers,
    following = following,
    publicRepos = publicRepos,
    avatarUrl = avatarUrl,
    profileUrl = profileUrl
)

fun UserInfoResponse.toDataLayer() = DataUserInfoResponse(
    login = login,
    followers = followers,
    following = following,
    publicRepos = publicRepos,
    avatarUrl = avatarUrl,
    profileUrl = profileUrl
)

