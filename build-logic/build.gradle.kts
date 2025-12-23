plugins {
    `kotlin-dsl`
}
group = "com.yamal.buildlogic"

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.ktlint.gradle)
    compileOnly(libs.compose.multiplatform.gradle)
    compileOnly(libs.compose.compiler.gradle)
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

        register("sharedPlugin") {
            id = "yamal.shared"
            implementationClass = "com.yamal.plugins.SharedMultiplatformModulePlugin"
        }

        register("androidApplicationPlugin") {
            id = "yamal.android.application"
            implementationClass = "com.yamal.plugins.AndroidApplicationConventionPlugin"
        }
    }
}
