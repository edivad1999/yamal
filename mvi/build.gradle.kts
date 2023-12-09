plugins {
    id("yamal.mvi")
}
android {
    namespace = "com.yamal.mvi"
}
kotlin {
    sourceSets.commonMain.dependencies {
        api(compose.runtime)
        api(compose.foundation)

    }
}
