plugins {
    id("yamal.presentation")
}
android {
    namespace = "com.yamal.presentation.animeDetails"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
    }
}
