import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.internal.utils.localPropertiesFile
import java.util.Properties

plugins {
    id("yamal.library")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
}
val localProperties = Properties().apply {
    load(localPropertiesFile.reader())
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.network.api)
        implementation(projects.feature.preferences.api)
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.contentNegotiation)
        implementation(libs.ktor.contentNegotiation.json)
        implementation(libs.ktor.auth)
        implementation(libs.ktor.logging)
    }
    sourceSets.androidMain.dependencies {
        implementation(libs.ktor.cio)
    }
    sourceSets.iosMain.dependencies {
        implementation(libs.ktor.ios)
    }
    sourceSets.desktopMain.dependencies {
        implementation(libs.ktor.cio)
    }
}
android {
    namespace = "com.yamal.feature.network.implementation"
}
buildkonfig {
    packageName = "com.yamal.feature.network.implementation"

    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "malClientId",
            value = localProperties.getProperty("malClientId")
        )
    }
}
