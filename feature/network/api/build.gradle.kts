plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
}
android {
    namespace = "com.yamal.feature.network.api"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
    }
}
