plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.network.api"
    }

    sourceSets.commonMain.dependencies {
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.paging.runtime)
    }
}
