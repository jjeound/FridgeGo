import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
    alias(libs.plugins.baselineprofile)
}

    android {
        namespace = "com.stone.fridge"
        compileSdk = 36

        defaultConfig {
            applicationId = "com.stone.fridge"
            minSdk = 26
            targetSdk = 36
            versionCode = 25
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                localProperties.load(localPropertiesFile.inputStream())
            }

            val kakaoAppKey = localProperties.getProperty("KAKAO_APP_KEY") ?: ""
            buildConfigField("String", "KAKAO_APP_KEY", "\"${localProperties["KAKAO_APP_KEY"]}\"")
            buildConfigField("String", "KAKAO_REST_API_KEY", "\"${localProperties["KAKAO_REST_API_KEY"]}\"")
            manifestPlaceholders["KAKAO_APP_KEY"] = kakaoAppKey
        }

        signingConfigs {

            val localProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                localProperties.load(localPropertiesFile.inputStream())
            }

            create("release") {
                storeFile = file(localProperties.getProperty("STORE_PATH") ?: "")
                storePassword  = localProperties.getProperty("STORE_PASSWORD") ?: ""
                keyAlias = localProperties.getProperty("KEY_ALIAS") ?: ""
                keyPassword = localProperties.getProperty("KEY_PASSWORD") ?: ""
            }
        }

        buildTypes {
            debug {
                isMinifyEnabled = false
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            release {
                signingConfig = signingConfigs["release"]
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlin {
            compilerOptions {
                freeCompilerArgs.add("-Xcontext-receivers")
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
                freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
            }
        }
        buildFeatures {
            compose = true
            buildConfig = true
        }
        hilt {
            enableAggregatingTask = true
        }
        packaging {
            resources.excludes.add("META-INF/DEPENDENCIES")
            resources.excludes.add("META-INF/INDEX.LIST")
        }
    }

dependencies {
    // Hilt
    implementation (libs.hilt.android)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
    kapt (libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    // Paging
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.paging.compose)
    implementation (libs.androidx.paging.common)

    // Room
    implementation (libs.androidx.room.ktx)
    ksp (libs.androidx.room.compiler)
    implementation (libs.androidx.room.paging)

    // FCM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.messaging.ktx)

    // login
    implementation (libs.v2.user)
    implementation (libs.androidx.datastore.preferences)
    implementation (libs.androidx.security.crypto)
    implementation (libs.android)
    implementation (libs.play.services.location)
    implementation (libs.play.services.maps)

    // CameraX
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    implementation (libs.androidx.camera.camera2)

    // ML Kit - 텍스트 인식
    implementation (libs.text.recognition)

    //Websocket
    implementation (libs.stompprotocolandroid)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}