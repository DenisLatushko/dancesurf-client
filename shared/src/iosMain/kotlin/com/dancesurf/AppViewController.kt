package com.dancesurf

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun AppViewController(): UIViewController = ComposeUIViewController {
    AppEntryPoint()
}
