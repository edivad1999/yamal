plugins {
    id("yamal.library")
}
kotlin {

    sourceSets.commonMain.dependencies {
        // Platform
        api(projects.platform.network.api)
        implementation(projects.platform.network.implementation)

        api(projects.platform.storage.api)
        implementation(projects.platform.storage.implementation)
        api(projects.platform.designsystem)

        // Features
        api(projects.features.login.api)
        implementation(projects.features.login.implementation)
        api(projects.features.login.ui)

        api(projects.features.anime.api)
        implementation(projects.features.anime.implementation)
        api(projects.features.anime.ui)
    }
}
android {
    namespace = "com.yamal.shared"
}
