plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.library.compose")
    alias(libs.plugins.kotlinx.serialization)
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.navigation"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)

    api(libs.androidx.navigation.compose)

    // json parsing
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}