plugins {
    id("stone.fridge.android.library")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.stone.fridge.core.database"
    defaultConfig {
        // The schemas directory contains a schema file for each version of the Room database.
        // This is required to enable Room auto migrations.
        // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

}

dependencies {
    implementation(projects.core.model)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.paging.compose.android)
    testImplementation(libs.kotlinx.coroutines.test)

    // database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Paging
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.room.paging)

    // json parsing
    implementation(libs.kotlinx.serialization.json)

    // unit test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}