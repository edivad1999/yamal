package com.yamal.platform.storage.implementation.extensions

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> Settings.delegate(
    defaultValue: T,
    key: String?,
    crossinline getter: Settings.(String, T) -> T,
    crossinline setter: Settings.(String, T) -> Unit,
): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getter(key ?: property.name, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            setter(key ?: property.name, value)
    }
}

fun Settings.int(defaultValue: Int = 0, key: String? = null) =
    delegate(
        defaultValue = defaultValue,
        key = key,
        getter = { keyOrProperty, _ -> getInt(keyOrProperty, defaultValue) },
        setter = { keyOrProperty, value ->
            set(keyOrProperty, value)
        }
    )

fun Settings.long(defaultValue: Long = 0, key: String? = null) =
    delegate(
        defaultValue = defaultValue,
        key = key,
        getter = { keyOrProperty, _ -> getLong(keyOrProperty, defaultValue) },
        setter = { keyOrProperty, value ->
            set(keyOrProperty, value)
        }
    )

fun Settings.float(defaultValue: Float = 0f, key: String? = null) =
    delegate(
        defaultValue = defaultValue,
        key = key,
        getter = { keyOrProperty, _ -> getFloat(keyOrProperty, defaultValue) },
        setter = { keyOrProperty, value ->
            set(keyOrProperty, value)
        }
    )

fun Settings.string(defaultValue: String = "", key: String? = null) =
    delegate(
        defaultValue = defaultValue,
        key = key,
        getter = { keyOrProperty, _ -> getString(keyOrProperty, defaultValue) },
        setter = { keyOrProperty, value ->
            set(keyOrProperty, value)
        }
    )

fun Settings.boolean(defaultValue: Boolean = false, key: String? = null) =
    delegate(
        defaultValue = defaultValue,
        key = key,
        getter = { keyOrProperty, _ -> getBoolean(keyOrProperty, defaultValue) },
        setter = { keyOrProperty, value ->
            set(keyOrProperty, value)
        }
    )
