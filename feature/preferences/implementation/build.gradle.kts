plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.preferences.api)
        implementation(projects.feature.network.api)
        implementation(libs.multiplatform.settings)
    }
}
android {
    namespace = "com.yamal.feature.preferences.implementation"
}
