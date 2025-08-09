import java.util.Properties

plugins {
    id("stone.fridge.android.application")
    id("stone.fridge.android.application.compose")
    id("stone.fridge.android.hilt")
    id("stone.fridge.spotless")
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
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
            create("benchmark"){
                matchingFallbacks += "release"
                signingConfig = signingConfigs["debug"]
                isDebuggable = false
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
    implementation(projects.feature.home)
    implementation(projects.feature.chat)
    implementation(projects.feature.fridge)
    implementation(projects.feature.post)
    implementation(projects.feature.my)
    implementation(projects.feature.notification)
    implementation(projects.feature.login)

    implementation(projects.core.ui)
    implementation(projects.core.auth)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.firebase)

    implementation(libs.androidx.adaptive.android)
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))

    ksp(libs.hilt.compiler)

    // FCM
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.messaging.ktx)

    // login
    implementation (libs.v2.user)
    implementation (libs.android)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material3.navigationSuite)

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