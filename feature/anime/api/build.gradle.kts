plugins {
    id("yamal.library")
}
android {
    namespace = "com.yamal.feature.anime.api"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.network.api)
        api(projects.core.utils)
    }
}
