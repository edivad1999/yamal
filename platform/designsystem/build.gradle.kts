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
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.compose)

        }
    }
}
dependencies {
    // https://youtrack.jetbrains.com/issue/KTIJ-32720/Support-common-org.jetbrains.compose.ui.tooling.preview.Preview-in-IDEA-and-Android-Studio#focus=Comments-27-11400795.0-0
    debugImplementation(libs.androidx.ui.tooling)
}
android {
    namespace = "com.yamal.designSystem"
    buildFeatures {
        compose = true
    }
}
