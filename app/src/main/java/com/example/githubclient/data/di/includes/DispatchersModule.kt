package com.example.githubclient.data.di.includes

import com.example.githubclient.constants.Constants.DISPATCHER_IO
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatchersModule = module {
    single(named(DISPATCHER_IO)) {
        Dispatchers.IO
    }
}