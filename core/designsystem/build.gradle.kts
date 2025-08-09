plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.library.compose")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.designsystem"
}

dependencies {
    api(libs.androidx.core.splashscreen)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.coil.compose)
}