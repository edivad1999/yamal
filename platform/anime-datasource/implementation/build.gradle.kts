plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.animeDatasource.api)
        implementation(projects.platform.network.api)
        implementation(projects.platform.jikanNetwork.api)
        implementation(projects.features.anime.api)
        implementation(projects.features.login.api)
        implementation(libs.arrow.core)
        implementation(libs.paging.runtime)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.koin.core)
    }
}

android {
    namespace = "com.yamal.platform.datasource.implementation"
}
