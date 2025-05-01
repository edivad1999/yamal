package com.yamal

import App
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import core.LoginUtilities

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

