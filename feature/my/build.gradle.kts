plugins {
    id("stone.fridge.android.feature")
    id("stone.fridge.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    namespace = "com.stone.fridge.feature.my"
}

dependencies {
    implementation (libs.v2.user)
    implementation(libs.androidx.paging.compose.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}