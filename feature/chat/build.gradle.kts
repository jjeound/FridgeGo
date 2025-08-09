plugins {
    id("stone.fridge.android.feature")
    id("stone.fridge.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.stone.fridge.feature.chat"
}

dependencies {
    implementation(libs.androidx.paging.compose.android)
}