package com.example.githubclient.data.di

import com.example.githubclient.data.di.includes.dispatchersModule
import com.example.githubclient.data.di.includes.repositoryModule
import com.example.githubclient.data.di.includes.retrofitModule

val dataModule = repositoryModule +
        retrofitModule +
        dispatchersModule