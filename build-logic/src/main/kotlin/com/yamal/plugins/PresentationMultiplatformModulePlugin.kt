package com.yamal.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class PresentationMultiplatformModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("yamal.mvi")
            }

            extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.androidMain.dependencies {
                    implementation(libs.findLibrary("koin-android").get())
                }
                sourceSets.commonMain.dependencies {
                    implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                    implementation(project(":mvi"))
                    implementation(libs.findBundle("arrow").get())
                    implementation(libs.findLibrary("napier").get())
                    implementation(libs.findLibrary("stately-common").get())

                    implementation(libs.findLibrary("paging-runtime").get())
                    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                    implementation(project(":presentation:core"))
                }
            }
        }
    }
}
