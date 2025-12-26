plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.platform.network.api)
        api(projects.platform.utils)
    }
}

android {
    namespace = "com.yamal.feature.anime.api"
}
