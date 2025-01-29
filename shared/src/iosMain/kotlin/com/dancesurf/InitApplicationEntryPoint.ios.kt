package com.dancesurf

import androidx.compose.runtime.Composable
import com.dancesurf.ui.theme.AppTheme
import com.dancesurf.utils.log.Log
import org.koin.compose.KoinContext

@Composable
actual fun InitApplicationEntryPoint(content: @Composable () -> Unit) {
    KoinContext {
        content()
    }
}