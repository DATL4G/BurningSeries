plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id ("kotlin-parcelize") apply false
}

group = "dev.datlag.burningseries.model"

kotlin {
    android()
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.arkivanov.essenty:parcelable:1.1.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }

        val androidMain by getting {
            apply(plugin = "kotlin-parcelize")
        }

        val jvmMain by getting

        val jsMain by getting
    }
}

android {
    sourceSets["main"].setRoot("src/androidMain/")

    compileSdk = Configuration.compileSdk
    buildToolsVersion = Configuration.buildTools

    defaultConfig {
        minSdk = Configuration.minSdk
    }

    compileOptions {
        sourceCompatibility = CompileOptions.sourceCompatibility
        targetCompatibility = CompileOptions.targetCompatibility
    }
}
