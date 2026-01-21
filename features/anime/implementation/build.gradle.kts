plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.anime.api)
        implementation(projects.platform.animeDatasource.api)
        implementation(projects.platform.utils)
    }
}

android {
    namespace = "com.yamal.feature.anime.implementation"
}
