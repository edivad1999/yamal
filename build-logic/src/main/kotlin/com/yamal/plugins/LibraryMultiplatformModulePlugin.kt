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
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
            }
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64(),
                )

                jvmToolchain(libs.findVersion("jdk").get().toString().toInt())
                androidTarget()
                jvm("desktop")
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                }
                sourceSets.create("nativeMain") {
                    dependencies {
                        implementation(libs.findLibrary("atomicfu").get())
                    }
                }
            }
        }
    }
}
