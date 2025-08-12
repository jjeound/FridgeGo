plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.common"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}