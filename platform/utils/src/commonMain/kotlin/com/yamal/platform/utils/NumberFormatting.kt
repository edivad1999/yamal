package com.yamal.platform.utils

import kotlin.math.roundToInt

/**
 * Formats a Float to a string with one decimal place.
 * KMP-compatible replacement for String.format("%.1f", value)
 */
fun Float.formatOneDecimal(): String {
    val rounded = (this * 10).roundToInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        "${rounded.toLong()}.0"
    } else {
        rounded.toString()
    }
}

/**
 * Formats a Double to a string with one decimal place.
 * KMP-compatible replacement for String.format("%.1f", value)
 */
fun Double.formatOneDecimal(): String {
    val rounded = (this * 10).roundToInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        "${rounded.toLong()}.0"
    } else {
        rounded.toString()
    }
}

/**
 * Formats a Double to a string with two decimal places.
 * KMP-compatible replacement for String.format("%.2f", value)
 */
fun Double.formatTwoDecimals(): String {
    val rounded = (this * 100).roundToInt() / 100.0
    val wholePart = rounded.toLong()
    val decimalPart = ((rounded - wholePart) * 100).roundToInt()
    return "$wholePart.${decimalPart.toString().padStart(2, '0')}"
}
