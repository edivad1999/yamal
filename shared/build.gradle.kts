plugins {
    id("yamal.library")
}
kotlin {

    sourceSets.commonMain.dependencies {
        // Presentation
        api(projects.presentation)

        // Platform
        api(projects.platform.network.api)
        implementation(projects.platform.network.implementation)

        api(projects.platform.storage.api)
        implementation(projects.platform.storage.implementation)
        api(projects.platform.designsystem)

        // Features
        api(projects.features.login.api)
        implementation(projects.features.login.implementation)

        api(projects.features.anime.api)
        implementation(projects.features.anime.implementation)
    }
}
android {
    namespace = "com.yamal.shared"
}
