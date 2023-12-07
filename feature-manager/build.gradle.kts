plugins {
    id("yamal.library")
}
kotlin {

    sourceSets.commonMain.dependencies {

        implementation(projects.feature.home.implementation)
        api(projects.feature.home.api)
        api(projects.presentation.home)
    }
}
android {
    namespace = "com.yamal.feature.manager"
}
