plugins {
    id("yamal.library")

}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.home.api)
    }
}
android {
    namespace = "com.yamal.feature.home.api"
}
