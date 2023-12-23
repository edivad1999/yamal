plugins {
    id("yamal.library")
}
android {
    namespace = "com.yamal.feature.preferences.api"
}
kotlin {
    sourceSets.commonMain.dependencies {

        implementation(projects.feature.network.api)
    }
}
