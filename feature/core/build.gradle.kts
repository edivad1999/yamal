plugins {
    id("yamal.library")
}

android {
    namespace = "com.yamal.feature.core"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.paging.runtime)
        implementation(projects.feature.network.api)
    }
}
