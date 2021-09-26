package com.example.githubclient.di

import com.example.githubclient.data.di.dataModule
import com.example.githubclient.domain.di.domainModule
import com.example.githubclient.presentation.di.presentationModule

val applicationModule = presentationModule +
        domainModule +
        dataModule