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
                sourceSets.commonMain.dependencies {
                    implementation(project(":mvi"))
                }
            }
        }
    }
}
