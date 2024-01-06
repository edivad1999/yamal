plugins {
    id("yamal.presentation")
}
android {
    namespace = "com.yamal.presentation.userAnimeList"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
        implementation(projects.feature.login.api)
    }
}
