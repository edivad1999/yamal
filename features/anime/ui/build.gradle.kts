plugins {
    id("yamal.mvi")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.utils)
        implementation(projects.platform.mvi)
        implementation(projects.platform.designsystem)
        implementation(projects.features.anime.api)
        implementation(projects.features.login.api)
        implementation(libs.paging.runtime)
        implementation(libs.paging.compose)
        api(compose.runtime)
        implementation(compose.material)
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.compose)
        implementation(libs.bundles.arrow)
        implementation(libs.napier)
        implementation(libs.kotlinx.datetime)
        implementation(libs.coil)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor3)
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.koin.android)
    }
}

android {
    namespace = "com.yamal.feature.anime.ui"
}
