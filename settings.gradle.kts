rootProject.name = "yamal"

include(":androidApp")
include(":shared")
include(":core")
include(":domain")

pluginManagement {
//    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }


}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
      google()
      mavenCentral()
      gradlePluginPortal()
    }
}


