plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.login.api)
        implementation(projects.core.network.api)
        implementation(projects.feature.utils)
        implementation(projects.feature.preferences.api)
    }
}
android {
    namespace = "com.yamal.feature.login.implementation"
}
