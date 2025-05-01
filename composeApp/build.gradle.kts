import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinCompose)
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
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

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {

            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.browser)
            api(libs.ktor.cio)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(project.dependencies.platform(libs.koin.bom))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.components.resources)
            implementation(libs.insetsx)
            implementation(libs.koin.compose)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.napier)
            implementation(libs.paging.runtime)
            implementation(libs.lifecycle.viewmodel.compose)

            implementation(libs.coil)
            implementation(libs.coil.network)
            implementation(libs.coil.compose)
        }

        sourceSets.iosMain.dependencies {
            api(libs.ktor.ios)
        }
    }
}

android {
    namespace = "com.yamal"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.yamal"
        versionCode = 1
        versionName = "1.0"
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileSdk = libs.versions.compileSdk.get().toInt()
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

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.yamal"
            packageVersion = "1.0.0"
        }
    }
}
