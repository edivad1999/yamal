plugins {
    id("yamal.shared")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.browser)
            api(libs.ktor.cio)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonMain.dependencies {
            // Platform
            api(projects.platform.network.api)
            implementation(projects.platform.network.implementation)
            api(projects.platform.storage.api)
            implementation(projects.platform.storage.implementation)
            implementation(projects.platform.designsystem)
            implementation(projects.platform.mvi)

            // Features
            implementation(projects.features.navigation)
            api(projects.features.login.api)
            implementation(projects.features.login.implementation)
            implementation(projects.features.login.ui)
            api(projects.features.anime.api)
            implementation(projects.features.anime.implementation)
            implementation(projects.features.anime.ui)

            implementation(project.dependencies.platform(libs.koin.bom))

            implementation(compose.runtime)
            implementation(compose.foundation)
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
    namespace = "com.yamal.shared"
}
