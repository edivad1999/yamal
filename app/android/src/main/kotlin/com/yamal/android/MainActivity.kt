package com.yamal.android

import App
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import core.LoginUtilities

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.auto(
                    lightScrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT,
                ),
            navigationBarStyle =
                SystemBarStyle.auto(
                    lightScrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT,
                ),
        )
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        if (intent.data?.toString()?.startsWith("yamal") == true) {
            intent.data?.getQueryParameter("code")?.let {
                LoginUtilities.parseUrlResult(it)
            }
        }
        super.onNewIntent(intent)
    }
}
