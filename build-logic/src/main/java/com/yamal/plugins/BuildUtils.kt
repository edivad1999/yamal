package com.yamal.plugins

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.yamal.plugins.extensions.configureKotlinMultiplatform
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.NamedDomainObjectCollectionDelegateProvider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


/**
 * This plugin will be used from core, domain and the kmm feature plugin
 */
class KotlinMultiplatformBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.native.cocoapods")
                apply("com.android.library")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinMultiplatform(this)
                defaultConfig.targetSdk = 33
            }

            val commonMain = extensions.create<EasyCommonMain>("easyDependencies")


        }
    }
}

interface EasyCommonMain {
    fun KotlinMultiplatformExtension.commonMain(block: KotlinDependencyHandler.() -> Unit): Unit {
        sourceSets["commonMain"].dependencies {
            block()
        }
    }

    fun KotlinMultiplatformExtension.iosMain(block: KotlinDependencyHandler.() -> Unit): Unit {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
        sourceSets {
            val commonMain = this["commonMain"]
            val iosX64Main = this["iosX64Main"]
            val iosArm64Main = this["iosArm64Main"]
            val iosSimulatorArm64Main = this["iosSimulatorArm64Main"]
            val iosMain = this.create("iosMain").apply {
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
                dependencies {
                    block()
                }
            }

        }


    }


    fun KotlinMultiplatformExtension.androidMain(block: KotlinDependencyHandler.() -> Unit): Unit {
        androidTarget()
        sourceSets["androidMain"]
            .dependencies {
                block()
            }
    }

    fun KotlinMultiplatformExtension.`sourceSets`(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
        (this as ExtensionAware).extensions.configure("sourceSets", configure)


}




