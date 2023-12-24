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
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")

include(":feature-manager")
include(":mvi")

// Presentation
include(":presentation:home")
include(":presentation:login")
// Features
include(":feature:home:api")
include(":feature:home:implementation")

include(":feature:network:api")
include(":feature:network:implementation")

include(":feature:preferences:api")
include(":feature:preferences:implementation")

include(":feature:login:api")
include(":feature:login:implementation")

include(":feature:anime:api")
include(":feature:anime:implementation")

include(":feature:core")
