import com.android.build.gradle.LibraryExtension
import com.stone.fridge.configureKotlinAndroid
import com.stone.fridge.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
      }

      dependencies {
        add("implementation", project(":core:designsystem"))
        add("implementation", project(":core:navigation"))
        add("implementation", project(":core:data"))
        add("implementation", project(":core:ui"))
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        configureAndroidCompose(this)
        defaultConfig.targetSdk = 36
      }

      extensions.getByType<KotlinAndroidProjectExtension>().apply {
        configureKotlinAndroid(this)
      }
    }
  }
}
