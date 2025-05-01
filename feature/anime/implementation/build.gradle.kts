plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
        implementation(projects.core.network.api)
    }
}
android {
    namespace = "com.yamal.feature.anime.implementation"
}
dependencies {
    implementation(projects.core.utils)
}
