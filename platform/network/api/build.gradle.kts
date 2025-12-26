plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.paging.runtime)
    }
}

android {
    namespace = "com.yamal.feature.network.api"
}
