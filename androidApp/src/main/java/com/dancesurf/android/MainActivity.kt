package com.dancesurf.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dancesurf.AppEntryPoint
import com.dancesurf.androidApp.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppEntryPoint(BuildConfig.DEBUG)
        }
    }
}
