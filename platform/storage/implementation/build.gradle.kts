plugins {
    id("yamal.library")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.preferences.implementation"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.platform.storage.api)
        implementation(projects.platform.network.api)
        implementation(libs.multiplatform.settings)
    }
}
