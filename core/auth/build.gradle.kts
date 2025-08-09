plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.auth"
}

dependencies {
    // core modules
    api(projects.core.model)

    implementation (libs.androidx.datastore.preferences)
    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    // unit test
    testImplementation(libs.junit)
}