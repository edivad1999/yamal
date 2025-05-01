plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.login.api)
        implementation(projects.core.network.api)
        implementation(projects.core.utils)
        implementation(projects.core.preferences.api)
    }
}
android {
    namespace = "com.yamal.feature.login.implementation"
}
