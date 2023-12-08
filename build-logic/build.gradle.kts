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
        register("kotlinMultiplatformPresentationPlugin") {
            id = "yamal.presentation"
            implementationClass = "com.yamal.plugins.PresentationMultiplatformModulePlugin"
        }
        register("kotlinMultiplatformMviPlugin") {
            id = "yamal.mvi"
            implementationClass = "com.yamal.plugins.MVIMultiplatformModulePlugin"
        }
    }
}
