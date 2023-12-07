package com.yamal.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class LibraryMultiplatformModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64(),
                )

                jvmToolchain(17)
                androidTarget()
                jvm("desktop")
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("koin-core").get())
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                }
            }
        }
    }
}
