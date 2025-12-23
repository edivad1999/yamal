plugins {
    id("yamal.library")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.anime.api"
    }

    sourceSets.commonMain.dependencies {
        api(projects.platform.network.api)
        api(projects.platform.utils)
    }
}
