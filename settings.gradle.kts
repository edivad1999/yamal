rootProject.name = "yamal"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://jogamp.org/deployment/maven")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
//        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
//        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

include(":composeApp")

include(":shared")

// Platform
include(":platform:mvi")
include(":platform:utils")
include(":platform:designsystem")

include(":platform:network:api")
include(":platform:network:implementation")

include(":platform:storage:api")
include(":platform:storage:implementation")

// Presentation
include(":presentation")

// Features
include(":features:login:api")
include(":features:login:implementation")

include(":features:anime:api")
include(":features:anime:implementation")
