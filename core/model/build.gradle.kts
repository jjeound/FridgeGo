plugins {
    id("stone.fridge.android.library")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    id("stone.fridge.spotless")
}

android {
    namespace = "com.stone.fridge.compose.core.model"
}

dependencies {
    // Kotlin Serialization for Json
    implementation(libs.kotlinx.serialization.json)
}