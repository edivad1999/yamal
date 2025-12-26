plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.paging.runtime)
        implementation(projects.platform.network.api)
    }
}

android {
    namespace = "com.yamal.feature.core"
}
