package com.dancesurf

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.KoinAndroidContext

@Composable
actual fun InitApplicationEntryPoint(content: @Composable () -> Unit) {
    KoinAndroidContext {
        content()
    }
}