rootProject.name = "yamal"

pluginManagement {
//    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

include(":androidApp")
include(":shared")
include(":core")
include(":domain")




