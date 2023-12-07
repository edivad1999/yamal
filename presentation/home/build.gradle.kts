plugins {
    id("yamal.compose")
    id ("kotlin-parcelize")


}
android {
    namespace = "com.yamal.presentation.home"
}
kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.feature.home.api)
    }
}
