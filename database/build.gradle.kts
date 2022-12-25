plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

group = "dev.datlag.burningseries.database"

kotlin {
    android()
    jvm("desktop")

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("11"))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.squareup.sqldelight:coroutines-extensions:1.5.4")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:1.5.4")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
            }
        }
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

sqldelight {
    database("BurningSeriesDB") {
        packageName = "dev.datlag.burningseries.database"
    }
}
