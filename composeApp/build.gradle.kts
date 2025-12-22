import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("yamal.application")
}

kotlin {

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
            implementation(projects.platform.designsystem)
            implementation(projects.platform.mvi)
            implementation(projects.features.navigation)
            implementation(projects.features.anime.ui)
            implementation(projects.features.login.ui)
            implementation(project.dependencies.platform(libs.koin.bom))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(libs.insetsx)
            implementation(libs.koin.compose)
            implementation(libs.koin.core)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.napier)
            implementation(libs.paging.runtime)
            implementation(libs.paging.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.navigation.compose)

            implementation(libs.coil)
            implementation(libs.coil.network.ktor3)
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
