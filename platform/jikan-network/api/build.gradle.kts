plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.datetime)
    }
}

android {
    namespace = "com.yamal.platform.jikan.api"
}
