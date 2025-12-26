plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.storage.api)
        implementation(projects.platform.network.api)
        implementation(libs.multiplatform.settings)
    }
}

android {
    namespace = "com.yamal.feature.preferences.implementation"
}
