plugins {
    id("yamal.compose")
}
android {
    namespace = "com.yamal.presentation.home"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.home.api)
    }
}
