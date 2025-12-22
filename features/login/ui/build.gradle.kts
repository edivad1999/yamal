plugins {
    id("yamal.mvi")
}

android {
    namespace = "com.yamal.feature.login.ui"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.mvi)
        implementation(projects.features.login.api)

        api(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
        implementation(compose.materialIconsExtended)
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
