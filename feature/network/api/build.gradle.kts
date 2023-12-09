plugins {
    id("yamal.library")
}
android {
    namespace = "com.yamal.feature.network.api"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.ktor.core)
    }
}
