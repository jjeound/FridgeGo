plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.data"
}

dependencies {
    // core modules
    api(projects.core.model)
    api(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.auth)
    api(projects.core.paging)

    implementation (libs.androidx.paging.common)
    implementation(libs.retrofit)
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.converter.gson)
    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    // unit test
    testImplementation(libs.junit)
}