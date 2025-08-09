pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/") }
    }
}

rootProject.name = "Untitled_Capstone"
include(":app")
include(":core:data")
include(":core:database")
include(":core:network")
include(":core:designsystem")
include(":core:model")
include(":core:navigation")
include(":feature:home")
include(":core:paging")
include(":core:auth")
include(":feature:chat")
include(":feature:fridge")
include(":feature:login")
include(":feature:my")
include(":feature:notification")
include(":feature:post")
include(":core:common")
include(":core:ui")
include(":core:firebase")
include(":baselineprofile")
