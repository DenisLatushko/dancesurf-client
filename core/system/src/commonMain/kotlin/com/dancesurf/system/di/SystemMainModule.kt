package com.dancesurf.system.di

import org.koin.core.module.Module
import org.koin.dsl.module

fun systemMainModule(): List<Module> = listOf(
    locationModule(), permissionModule()
)

internal expect fun locationModule(): Module

internal expect fun permissionModule(): Module