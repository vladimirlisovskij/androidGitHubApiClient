package com.example.githubclient.data.di.includes

import com.example.githubclient.constants.Constants.DISPATCHER_IO
import com.example.githubclient.data.reposioty.ApiRepositoryImpl
import com.example.githubclient.data.reposioty.SharedPreferencesRepositoryImpl
import com.example.githubclient.domain.repository.ApiRepository
import com.example.githubclient.domain.repository.SharedPreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    factory<ApiRepository> {
        ApiRepositoryImpl(
            githubApiService = get()
        )
    }

    factory<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(
            dispatcherIo = get(named(DISPATCHER_IO)),
            context = androidContext()
        )
    }
}
