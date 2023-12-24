package com.yamal

import App
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import screen.login.LoginUtilities

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent?.data?.toString()?.startsWith("yamal") == true) {
            intent.data?.getQueryParameter("code")?.let {
                LoginUtilities.parseUrlResult(it)
            }
        }
        super.onNewIntent(intent)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
