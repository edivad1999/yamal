package com.yamal.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
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
            val extension = extensions.create<ExportFramework>("exportFramework")
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                val ios = listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64(),
                )
                if (extension.frameworkName.isNotBlank() && extension.exportFramework) {
                    ios.forEach { iosTarget ->
                        iosTarget.binaries.framework {
                            baseName = extension.frameworkName
                            isStatic = true
                        }
                    }
                }
                jvmToolchain(17)
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                androidTarget()
                jvm("desktop")
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("koin-core").get())
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
                }
            }
        }
    }
}
