package com.yamal.plugins

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MVIMultiplatformModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("yamal.ktlint")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidLibrary {
                    compileSdk = compileSdkVersion
                    minSdk = minSdkVersion
                }
                jvmToolchain(jdkVersion)
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                jvm("desktop")

                sourceSets.commonMain.dependencies {
                    implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                    implementation(libs.findBundle("arrow").get())
                    implementation(libs.findLibrary("napier").get())
                }
            }
        }
    }
}
