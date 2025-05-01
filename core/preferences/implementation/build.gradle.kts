plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.preferences.api)
        implementation(projects.core.network.api)
        implementation(libs.multiplatform.settings)
    }
}
android {
    namespace = "com.yamal.feature.preferences.implementation"
}
