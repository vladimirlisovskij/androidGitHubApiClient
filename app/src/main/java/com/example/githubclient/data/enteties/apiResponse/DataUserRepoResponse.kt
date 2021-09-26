package com.example.githubclient.data.enteties.apiResponse

import com.example.githubclient.domain.enteties.apiResponse.UserRepoResponse
import com.google.gson.annotations.SerializedName

data class DataUserRepoResponse(
    val name: String,
    @SerializedName("created_at") val created: String,
    @SerializedName("updated_at") val updated: String,
    @SerializedName("pushed_at") val pushed: String,
    val language: String?,
    @SerializedName("html_url") val url: String
)

fun DataUserRepoResponse.toDomainLayer() = UserRepoResponse(
    name = name,
    created = created,
    updated = updated,
    pushed = pushed,
    language = language,
    url = url
)

fun UserRepoResponse.toDataLayer() = DataUserRepoResponse(
    name = name,
    created = created,
    updated = updated,
    pushed = pushed,
    language = language,
    url = url
)