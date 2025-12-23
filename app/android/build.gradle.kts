plugins {
    id("yamal.android.application")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(projects.app.shared)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
    implementation(libs.koin.android)
}
