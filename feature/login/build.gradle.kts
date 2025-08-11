plugins {
    id("stone.fridge.android.feature")
    id("stone.fridge.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    namespace = "com.stone.fridge.feature.login"
}

dependencies {
    // login
    implementation (libs.v2.user)
    implementation (libs.android)
    implementation (libs.play.services.location)
    implementation (libs.play.services.maps)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}