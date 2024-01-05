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
        maven("https://oss.sonatype.org/content/repositories/snapshots")
//        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
//        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

include(":composeApp")

include(":shared")
include(":mvi")

// Presentation
include(":presentation:home")
include(":presentation:animeRanking")
include(":presentation:animeSeasonal")
include(":presentation:userAnimeList")
include(":presentation:login")
// Features
include(":feature:network:api")
include(":feature:network:implementation")

include(":feature:preferences:api")
include(":feature:preferences:implementation")

include(":feature:login:api")
include(":feature:login:implementation")

include(":feature:anime:api")
include(":feature:anime:implementation")

include(":feature:core")
