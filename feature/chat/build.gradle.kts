plugins {
    id("stone.fridge.android.feature")
    id("stone.fridge.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.stone.fridge.feature.chat"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.paging.compose.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}