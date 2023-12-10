plugins {
    id("yamal.presentation")
}
android {
    namespace = "com.yamal.presentation.login"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.login.api)
    }
}
