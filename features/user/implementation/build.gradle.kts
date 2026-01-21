plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.user.api)
        implementation(projects.platform.network.api)

        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.core)
        implementation(libs.bundles.arrow)
    }
}

android {
    namespace = "com.yamal.feature.user.implementation"
}
