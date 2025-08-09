plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.firebase"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    // FCM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.messaging.ktx)
    implementation(libs.androidx.core.ktx)
    implementation (libs.androidx.datastore.preferences)


    testImplementation(libs.junit)
}