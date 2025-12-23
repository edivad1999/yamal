package com.yamal.plugins

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.jdkVersion
    get() =
        libs
            .findVersion("jdk")
            .get()
            .toString()
            .toInt()
val Project.compileSdkVersion
    get() =
        libs
            .findVersion("compileSdk")
            .get()
            .toString()
            .toInt()
val Project.minSdkVersion
    get() =
        libs
            .findVersion("minSdk")
            .get()
            .toString()
            .toInt()
