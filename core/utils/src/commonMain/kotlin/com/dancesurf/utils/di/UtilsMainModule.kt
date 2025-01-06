package com.dancesurf.utils.di

import com.dancesurf.utils.BuildConfig
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun utilsMainModule(isDebug: Boolean): Module = module {
    factory {
        BuildConfig(
            isDebug = isDebug
        )
    }
}