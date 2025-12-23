plugins {
    id("yamal.library")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.feature.core"
    }

    sourceSets.commonMain.dependencies {
        implementation(libs.paging.runtime)
        implementation(projects.platform.network.api)
    }
}
