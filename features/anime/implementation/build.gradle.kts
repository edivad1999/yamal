plugins {
    id("yamal.library")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.anime.implementation"
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.features.anime.api)
        implementation(projects.platform.network.api)
        implementation(projects.platform.utils)
    }
}
