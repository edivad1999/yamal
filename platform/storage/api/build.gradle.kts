plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.platform.network.api)
    }
}

android {
    namespace = "com.yamal.feature.preferences.api"
}
