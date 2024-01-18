plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.anime.api)
        implementation(projects.feature.network.api)
    }
}
android {
    namespace = "com.yamal.feature.anime.implementation"
}
dependencies {
    implementation(project(":feature:core"))
}
