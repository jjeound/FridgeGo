plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.domain"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}