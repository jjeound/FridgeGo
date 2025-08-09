plugins {
    id("stone.fridge.android.feature")
    id("stone.fridge.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    namespace = "com.stone.fridge.feature.fridge"
}

dependencies {
    implementation(libs.androidx.paging.compose.android)
    // CameraX
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    implementation (libs.androidx.camera.camera2)

    // ML Kit - 텍스트 인식
    implementation (libs.text.recognition)
}