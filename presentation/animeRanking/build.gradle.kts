plugins {
    id("yamal.presentation")
}
android {
    namespace = "com.yamal.presentation.animeRanking"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
    }
}
