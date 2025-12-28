plugins {
    id("yamal.library")
    id("yamal.designSystem.icons")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinCompose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            implementation(compose.material)
            implementation(compose.components.resources)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
        }
    }
}

android {
    androidResources.enable = true
    namespace = "com.yamal.designSystem"
}

dependencies {
//    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(compose.uiTooling)
}

compose.resources {
}
