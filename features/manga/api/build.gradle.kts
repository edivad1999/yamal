plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.network.api)
        implementation(projects.platform.utils)
        implementation(libs.bundles.arrow)
    }
}

android {
    namespace = "com.yamal.feature.manga.api"
}
