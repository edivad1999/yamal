plugins {
    id("yamal.library")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.preferences.api"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.platform.network.api)
    }
}
