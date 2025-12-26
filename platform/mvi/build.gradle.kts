plugins {
    id("yamal.mvi")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(compose.runtime)
        api(libs.lifecycle.viewmodel.compose)

        implementation(compose.foundation)
    }
}

android {
    namespace = "com.yamal.mvi"
}
