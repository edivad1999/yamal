package com.yamal.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                jvmToolchain(17)
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                androidTarget()
                jvm("desktop")
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("koin-core").get())
                    implementation("com.slack.circuit:circuit-foundation:0.17.1")
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
                }
            }
        }
    }
}
