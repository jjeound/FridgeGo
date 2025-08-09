plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.stone.fridge.core.paging"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.database)

    // Paging
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.common)

    // Room
    implementation (libs.androidx.room.ktx)
    implementation (libs.androidx.room.paging)

    // KSP Compiler
    ksp(libs.androidx.room.compiler)

    implementation(libs.retrofit)

    testImplementation(libs.junit)
}