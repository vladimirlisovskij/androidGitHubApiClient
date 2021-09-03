package com.example.githubclient.domain.repository

interface SharedPreferencesRepository {
    suspend fun saveUserId(userId: String)

    suspend fun clearUserId()

    suspend fun loadUserId(): String?
}