plugins {
    id("yamal.presentation")
}
android {
    namespace = "com.yamal.presentation.home"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
    }
}
