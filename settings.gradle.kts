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

// App modules
include(":app:shared")
include(":app:android")
include(":app:desktop")

// Platform
include(":platform:mvi")
include(":platform:utils")
include(":platform:designsystem")

include(":platform:network:api")
include(":platform:network:implementation")

include(":platform:jikan-network:api")
include(":platform:jikan-network:implementation")

include(":platform:anime-datasource:api")
include(":platform:anime-datasource:implementation")

include(":platform:storage:api")
include(":platform:storage:implementation")

// Features
include(":features:navigation")

include(":features:login:api")
include(":features:login:implementation")
include(":features:login:ui")

include(":features:anime:api")
include(":features:anime:implementation")
include(":features:anime:ui")

include(":features:search:api")
include(":features:search:implementation")
include(":features:search:ui")

include(":features:user:api")
include(":features:user:implementation")
include(":features:user:ui")

include(":features:manga:api")
include(":features:manga:implementation")
include(":features:manga:ui")
