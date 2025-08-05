plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.core.paging"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.database)

    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.common)
    implementation(libs.retrofit)
    implementation(libs.androidx.room.paging)

    testImplementation(libs.junit)
}