package com.yamal.desktop

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.koinInitializer

fun main() =
    application {
        koinInitializer { }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Yamal",
        ) {
            App()
        }
    }
