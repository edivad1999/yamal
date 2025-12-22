plugins {
    `kotlin-dsl`
}
group = "com.yamal.buildlogic"

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.ktlint.gradle)
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

        register("ktlintPlugin") {
            id = "yamal.ktlint"
            implementationClass = "com.yamal.plugins.KtlintConventionPlugin"
        }

        register("applicationPlugin") {
            id = "yamal.application"
            implementationClass = "com.yamal.plugins.ApplicationConventionPlugin"
        }
    }
}
