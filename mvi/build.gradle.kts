plugins {
    id("yamal.mvi")
    kotlin("plugin.serialization")
}
android {
    namespace = "com.yamal.mvi"
}
kotlin {
    sourceSets.commonMain.dependencies {
        api(compose.runtime)
        api(compose.foundation)
        implementation(libs.kotlinx.serialization.json)
    }
}
