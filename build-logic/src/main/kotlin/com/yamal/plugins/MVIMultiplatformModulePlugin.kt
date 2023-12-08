package com.yamal.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MVIMultiplatformModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
            }
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                jvmToolchain(libs.findVersion("jdk").get().toString().toInt())
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                androidTarget()
                jvm("desktop")
                sourceSets.getByName("desktopMain").dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0-RC")
                }
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("voyager-navigator").get())
                    implementation(libs.findLibrary("voyager-koin").get())
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
