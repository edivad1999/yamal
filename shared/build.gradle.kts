plugins {
    id("yamal.library")
}
kotlin {

    sourceSets.commonMain.dependencies {
        // Presentation
        api(projects.presentation.home)
        api(projects.presentation.login)

        // Features

        api(projects.feature.network.api)
        implementation(projects.feature.network.implementation)

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
