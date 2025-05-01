plugins {
    `kotlin-dsl`
}
group = "com.yamal.buildlogic"

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
}
gradlePlugin {
    plugins {

        register("kotlinMultiplatformLibraryPlugin") {
            id = "yamal.library"
            implementationClass = "com.yamal.plugins.LibraryMultiplatformModulePlugin"
        }

        register("kotlinMultiplatformMviPlugin") {
            id = "yamal.mvi"
            implementationClass = "com.yamal.plugins.MVIMultiplatformModulePlugin"
        }
    }
}
