plugins {
    id("yamal.mvi")
}

kotlin {
    androidLibrary {
        namespace = "com.yamal.mvi"
    }

    sourceSets.commonMain.dependencies {
        api(compose.runtime)
        api(libs.lifecycle.viewmodel.compose)

        implementation(compose.foundation)
    }
}
