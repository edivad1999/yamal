plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.login.api)
        implementation(projects.feature.network.api)
        implementation(projects.feature.core)
        implementation(projects.feature.preferences.api)
    }
}
android {
    namespace = "com.yamal.feature.login.implementation"
}
