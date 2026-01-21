plugins {
    id("yamal.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.bundles.arrow)
    }
}

android {
    namespace = "com.yamal.feature.user.api"
}
