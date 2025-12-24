plugins {
    id("yamal.library")
    id("yamal.designSystem.icons")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinCompose)
}

kotlin {

    androidLibrary {
        androidResources.enable = true
        namespace = "com.yamal.designSystem"
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            implementation(compose.components.resources)
            api(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
        }
    }
}
dependencies {
    // https://youtrack.jetbrains.com/issue/KTIJ-32720/Support-common-org.jetbrains.compose.ui.tooling.preview.Preview-in-IDEA-and-Android-Studio#focus=Comments-27-11400795.0-0
    // For the new Android-KMP plugin, use androidRuntimeClasspath instead of debugImplementation
    "androidRuntimeClasspath"(libs.androidx.ui.tooling)
}
compose.resources {
    publicResClass = true
}
