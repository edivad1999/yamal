plugins {
    id("yamal.mvi")
}

android {
    namespace = "com.yamal.presentation"
}

kotlin {
    sourceSets.commonMain.dependencies {

        implementation(projects.feature.utils)
        implementation(projects.core.network.api)
        implementation(libs.paging.runtime)
        api(compose.runtime)
        implementation(compose.foundation)
        implementation(project.dependencies.platform(libs.koin.bom))

        implementation(project(":mvi"))
        implementation(libs.bundles.arrow)
        implementation(libs.napier)
        implementation(libs.paging.runtime)

        implementation(libs.kotlinx.datetime)

        // Dependencies from individual modules
        implementation(projects.feature.login.api)
        implementation(projects.feature.anime.api)
        // Add any other feature dependencies that were in the individual modules
    }

    sourceSets.androidMain.dependencies {
        implementation(libs.koin.android)
    }
}
