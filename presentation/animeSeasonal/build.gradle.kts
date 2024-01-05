plugins {
    id("yamal.presentation")
}
android {
    namespace = "com.yamal.presentation.animeSeasonal"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
    }
}
