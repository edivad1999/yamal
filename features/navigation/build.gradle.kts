plugins {
    id("yamal.mvi")
}

android {
    namespace = "com.yamal.feature.navigation"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)

        // Navigation
        implementation(libs.navigation.compose)

        // Feature UI modules
        implementation(projects.features.anime.ui)
        implementation(projects.features.login.ui)

        // Platform modules
        implementation(projects.platform.designsystem)
    }
}
