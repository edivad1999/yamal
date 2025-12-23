plugins {
    id("yamal.library")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinCompose)
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.designSystem"
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material)
            api(compose.materialIconsExtended)
            api(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.compose)
        }
    }
}
dependencies {
    // https://youtrack.jetbrains.com/issue/KTIJ-32720/Support-common-org.jetbrains.compose.ui.tooling.preview.Preview-in-IDEA-and-Android-Studio#focus=Comments-27-11400795.0-0
    // For the new Android-KMP plugin, use androidRuntimeClasspath instead of debugImplementation
    "androidRuntimeClasspath"(libs.androidx.ui.tooling)
}
