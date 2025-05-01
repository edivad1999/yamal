
plugins {
    id("yamal.mvi")
}
android {
    namespace = "com.yamal.presentation.core"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.utils)
        implementation(projects.core.network.api)
        implementation(libs.paging.runtime)
        api(compose.runtime)
        implementation(compose.foundation)
    }
}
