package com.yamal.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("yamal.ktlint")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                jvmToolchain(17)
                androidTarget()
                jvm("desktop")

                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64(),
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                    }
                }
            }

            extensions.configure<BaseAppModuleExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 24
                    targetSdk = 36
                }

                buildFeatures {
                    compose = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}