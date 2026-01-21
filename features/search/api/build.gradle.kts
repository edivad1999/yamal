plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.platform.network.api)
        api(projects.platform.utils)
        api(projects.features.anime.api)
    }
}

android {
    namespace = "com.yamal.feature.search.api"
}
