plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.anime.api)
        implementation(libs.arrow.core)
        implementation(libs.paging.runtime)
    }
}

android {
    namespace = "com.yamal.platform.datasource.api"
}
