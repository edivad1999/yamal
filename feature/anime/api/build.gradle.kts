plugins {
    id("yamal.library")
}
android {
    namespace = "com.yamal.feature.anime.api"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.network.api)
        api(libs.paging.runtime)
    }
}
