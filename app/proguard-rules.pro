# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ML Kit (Text Recognition)
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter

# Retrofit & OkHttp
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# CameraX
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# Hilt
-keep class dagger.** { *; }
-dontwarn dagger.**

# DataStore
-keep class androidx.datastore.** { *; }

# Stomp Protocol
-keep class ua.naiksoftware.stomp.** { *; }
-dontwarn ua.naiksoftware.stomp.**

# Coil
-keep class coil.** { *; }

# Kotlin metadata
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature, SourceFile, LineNumberTable

# Needed for AD_ID safe use
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { *; }

# Keep the classes used by the AndroidX Navigation component
-keep class com.stone.fridge.data.remote.dto.** { *; }
-keep @kotlinx.serialization.Serializable class * { *; }
-keep class kotlinx.serialization.** { *; }

-keep class com.kakao.sdk.**.model.* { <fields>; }

# https://github.com/square/okhttp/pull/6792
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# refrofit2 (with r8 full mode)
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep class com.kakao.vectormap.** { *; }
-keep interface com.kakao.vectormap.**