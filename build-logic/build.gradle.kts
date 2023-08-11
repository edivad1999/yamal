plugins {
    `kotlin-dsl`
}
group = "com.yamal.buildlogic"
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}
//gradlePlugin{
//    plugins {
//        register("kotlinMultiplatformBasePlugin") {
//            id = "yamal.kmm.base"
//            implementationClass = "com.yamal.plugins.KotlinMultiplatformBasePlugin"
//        }
//
//        register("androidLibraryPlugin") {
//            id = "yamal.android.library"
//            implementationClass = "com.yamal.plugins.AndroidLibraryPlugin"
//        }
//        register("domainPlugin") {
//            id = "yamal.domain"
//            implementationClass = "com.yamal.plugins.DomainPlugin"
//        }
//        register("corePlugin") {
//            id = "yamal.core"
//            implementationClass = "com.yamal.plugins.CorePlugin"
//        }
//        register("sharedPlugin") {
//            id = "yamal.shared"
//            implementationClass = "com.yamal.plugins.SharedPlugin"
//        }
//    }
//}