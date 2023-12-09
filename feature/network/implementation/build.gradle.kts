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
        implementation(projects.feature.network.api)
        implementation(libs.ktor.core)
        implementation(libs.kotlinx.serialization.json)
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
            FieldSpec.Type.STRING, "malClientId", localProperties.getProperty("malClientId")
        )

    }

}
