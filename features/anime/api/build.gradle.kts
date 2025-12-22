plugins {
    id("yamal.library")
}
android {
    namespace = "com.yamal.feature.anime.api"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.platform.network.api)
        api(projects.platform.utils)
    }
}
