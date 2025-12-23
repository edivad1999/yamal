plugins {
    id("yamal.library")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.login.api"
    }

    sourceSets.commonMain.dependencies {
    }
}
