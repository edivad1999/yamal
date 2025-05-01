plugins {
    id("yamal.library")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinCompose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.compose)

        }
    }
}

android {
    namespace = "com.yamal.designSystem"
    buildFeatures {
        compose = true
    }
}
