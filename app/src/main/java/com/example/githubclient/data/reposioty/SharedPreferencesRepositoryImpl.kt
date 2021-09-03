package com.example.githubclient.data.reposioty

import android.content.Context
import androidx.core.content.edit
import com.example.githubclient.domain.repository.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SharedPreferencesRepositoryImpl(
    private val context: Context,
    private val dispatcherIo: CoroutineDispatcher
): SharedPreferencesRepository {
    companion object {
        const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
    }

    private val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    override suspend fun saveUserId(userId: String) {
        withContext(dispatcherIo) {
            prefs.edit {
                putString(USER_ID_KEY, userId)
            }
        }
    }

    override suspend fun clearUserId() {
        withContext(dispatcherIo) {
            prefs.edit {
                remove(USER_ID_KEY)
            }
        }
    }

    override suspend fun loadUserId(): String? {
        return withContext(dispatcherIo) {
            prefs.getString(USER_ID_KEY, null)
        }
    }
}