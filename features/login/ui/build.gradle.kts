plugins {
    id("yamal.mvi")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.mvi)
        implementation(projects.features.login.api)
        implementation(projects.platform.designsystem)
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.compose)
        implementation(libs.bundles.arrow)
        implementation(libs.napier)
        implementation(libs.kotlinx.coroutines.core)
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.koin.android)
    }
}

android {
    namespace = "com.yamal.feature.login.ui"
}
