plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.anime.api)
        implementation(projects.platform.network.api)
    }
}
android {
    namespace = "com.yamal.feature.anime.implementation"
}
dependencies {
    implementation(projects.platform.utils)
}
