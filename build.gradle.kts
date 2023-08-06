@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false) version libs.versions.kotlinVersion
    id("com.android.application").apply(false) version libs.versions.agpVersion
    id("com.android.library").apply(false) version libs.versions.agpVersion
    id("org.jetbrains.compose").apply(false) version libs.versions.composeVersion
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
}
