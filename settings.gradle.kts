rootProject.name = "Burning-Series"

include(":app:shared")
include(":app:shared:sekret")
include(":app:android")
include(":app:desktop")

include(":model")
include(":network")
include(":color")
include(":database")

include(":extension")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven("https://jogamp.org/deployment/maven")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}