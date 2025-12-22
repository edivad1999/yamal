package com.yamal.platform.utils

object PKCEGenerator {

    private val urlSafeChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + ("-._".toCharArray().toList())

    fun generate(length: Int): String = (1..length).map {
        urlSafeChars.random()
    }.joinToString("")
}
