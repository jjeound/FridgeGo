plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.library.compose")
}

android {
    namespace = "com.stone.fridge.core.ui"
}

dependencies {
    api(projects.core.designsystem)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}