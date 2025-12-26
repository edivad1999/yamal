plugins {
    id("yamal.mvi")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)

        // Navigation
        implementation(libs.navigation.compose)

        // Serialization for type-safe navigation
        implementation(libs.kotlinx.serialization.json)

        // Feature UI modules
        implementation(projects.features.anime.ui)
        implementation(projects.features.login.ui)

        // Platform modules
        implementation(projects.platform.designsystem)
    }
}

android {
    namespace = "com.yamal.feature.navigation"
}
