plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.manga.api)
        implementation(projects.platform.network.api)
        implementation(projects.platform.utils)

        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.core)
        implementation(libs.bundles.arrow)
    }
}

android {
    namespace = "com.yamal.feature.manga.implementation"
}
