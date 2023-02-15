@file:Suppress("UnstableApiUsage")
import java.util.Properties

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

includeBuild("build-logic")

include(":common")
include(":firebase-auth-ktx")
include(":signin-domain")
include(":signin-android-compose-presentation")
include(":sample-app")
include(":sample-app-ui")
