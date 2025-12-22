plugins {
    id("yamal.mvi")
}

android {
    namespace = "com.yamal.presentation"
}

kotlin {
    sourceSets.commonMain.dependencies {

        implementation(projects.platform.utils)
        implementation(projects.platform.network.api)
        implementation(libs.paging.runtime)
        api(compose.runtime)
        implementation(compose.foundation)
        implementation(project.dependencies.platform(libs.koin.bom))

        implementation(projects.platform.mvi)
        implementation(libs.bundles.arrow)
        implementation(libs.napier)
        implementation(libs.paging.runtime)

        implementation(libs.kotlinx.datetime)

        // Dependencies from individual modules
        implementation(projects.features.login.api)
        implementation(projects.features.anime.api)
        // Add any other feature dependencies that were in the individual modules
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.koin.android)
    }
}
