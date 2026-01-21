plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.jikanNetwork.api)
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.ktor.contentNegotiation)
        implementation(libs.ktor.contentNegotiation.json)
        implementation(libs.ktor.logging)
        implementation(libs.kotlinx.datetime)
        implementation(libs.koin.core)
        implementation(libs.napier)
    }
    sourceSets.androidMain.dependencies {
        implementation(libs.ktor.cio)
    }
    sourceSets.iosMain.dependencies {
        implementation(libs.ktor.ios)
    }
    sourceSets.desktopMain.dependencies {
        implementation(libs.ktor.cio)
    }
}

android {
    namespace = "com.yamal.platform.jikan.implementation"
}
