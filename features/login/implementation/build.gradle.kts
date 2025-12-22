plugins {
    id("yamal.library")
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.login.api)
        implementation(projects.platform.network.api)
        implementation(projects.platform.utils)
        implementation(projects.platform.storage.api)
    }
}
android {
    namespace = "com.yamal.feature.login.implementation"
}
