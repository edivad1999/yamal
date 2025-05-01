plugins {
    id("yamal.library")
}
kotlin {

    sourceSets.commonMain.dependencies {
        // Presentation
        api(projects.presentation)

        // Features

        api(projects.core.network.api)
        implementation(projects.core.network.implementation)

        api(projects.feature.login.api)
        implementation(projects.feature.login.implementation)

        api(projects.feature.anime.api)
        implementation(projects.feature.anime.implementation)

        api(projects.feature.preferences.api)
        implementation(projects.feature.preferences.implementation)
    }
}
android {
    namespace = "com.yamal.shared"
}
