package com.dancesurf

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun AppViewController(isDebug: Boolean): UIViewController =
    ComposeUIViewController { AppEntryPoint(isDebug) }
