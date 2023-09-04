rootProject.name = "yamal"

include(":androidApp")
include(":shared")
include(":core")
include(":domain")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    }


}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {

      google()
      mavenLocal()
      mavenCentral()
      gradlePluginPortal()
      maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}


