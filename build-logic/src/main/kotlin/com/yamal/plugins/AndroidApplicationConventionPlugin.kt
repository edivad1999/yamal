package com.yamal.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("yamal.ktlint")
            }

            extensions.configure<KotlinAndroidExtension> {
                jvmToolchain(libs.findVersion("jdk").get().toString().toInt())
            }
            extensions.configure<BaseAppModuleExtension> {
                namespace = "com.yamal.android"
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

                defaultConfig {
                    applicationId = "com.yamal"
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                    versionCode = 1
                    versionName = "1.0"
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
