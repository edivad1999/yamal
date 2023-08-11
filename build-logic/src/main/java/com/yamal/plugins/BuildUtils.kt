package com.yamal.plugins

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions


/**
 * This plugin will be used from core, domain and the kmm feature plugin
 */
class KotlinMultiplatformBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.native.cocoapods")
                apply("yamal.android.library")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = 33

            }
        }
    }
}
class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = 33
            }

            configurations.configureEach {
                resolutionStrategy {}
            }
            dependencies {}
        }
    }
}

class CorePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("yamal.kmm.base")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = 33
                compileSdk = 33
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("commonMainImplementation", libs.findLibrary("ktor.client.core").get())
                add("commonMainImplementation", libs.findLibrary("ktor.client.contentNegotiation").get())
                add("commonMainImplementation", libs.findLibrary("ktor.client.serialization.json").get())
                add("commonMainImplementation", libs.findLibrary("kotlinx.serialization.json").get())
                add("commonMainImplementation", libs.findLibrary("coroutines.core").get())
                add("androidMainImplementation", libs.findLibrary("coroutines.android").get())
                add("androidMainImplementation", libs.findLibrary("ktor.client.android").get())
                add("iosMainImplementation", libs.findLibrary("libs.ktor.client.darwin").get())
            }
        }
    }
}
class DomainPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("yamal.kmm.base")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = 33
                compileSdk = 33
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("commonMainImplementation", libs.findLibrary("ktor.client.core").get())
                add("commonMainImplementation", libs.findLibrary("ktor.client.contentNegotiation").get())
                add("commonMainImplementation", libs.findLibrary("ktor.client.serialization.json").get())
                add("commonMainImplementation", libs.findLibrary("kotlinx.serialization.json").get())
                add("commonMainImplementation", libs.findLibrary("coroutines.core").get())
                add("androidMainImplementation", libs.findLibrary("coroutines.android").get())
                add("androidMainImplementation", libs.findLibrary("ktor.client.android").get())
                add("iosMainImplementation", libs.findLibrary("libs.ktor.client.darwin").get())
            }
        }
    }
}
