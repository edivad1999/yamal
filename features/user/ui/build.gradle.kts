plugins {
    id("yamal.mvi")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.user.api)
        implementation(projects.features.login.api)
        implementation(projects.platform.mvi)
        implementation(projects.platform.designsystem)
        implementation(projects.platform.utils)

        api(compose.runtime)
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.compose)
        implementation(libs.bundles.arrow)
        implementation(libs.coil)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor3)
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.koin.android)
    }
}

android {
    namespace = "com.yamal.feature.user.ui"
}
