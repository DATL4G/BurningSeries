import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.application")
    id("kotlin-parcelize") apply false
    id("com.mikepenz.aboutlibraries.plugin")
}

val coroutines = "1.6.4"
val decompose = "1.0.0-beta-02"
val kodein = "7.16.0"
val ktor = "2.2.1"
val exoplayer = "1.0.0-beta03"
val accompanist = "0.25.1"

val artifact = "dev.datlag.burningseries"
val appVersion = "4.0.0"
val appCode = 400

group = artifact
version = appVersion

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    android()
    jvm("desktop")

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("11"))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.material3)
                api(compose.materialIconsExtended)

                api("com.arkivanov.decompose:decompose:$decompose")
                api("com.arkivanov.decompose:extensions-compose-jetbrains:$decompose")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
                api("org.kodein.di:kodein-di:$kodein")
                implementation("org.kodein.di:kodein-di-framework-compose:$kodein")
                api("io.ktor:ktor-client-okhttp:$ktor")
                api("io.ktor:ktor-client-content-negotiation:$ktor")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktor")
                api("com.squareup.okhttp3:okhttp-dnsoverhttps:4.10.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                implementation("com.mikepenz:aboutlibraries-compose:10.5.2")
                implementation("com.mikepenz:aboutlibraries-core:10.5.2")

                implementation(project(":network"))
                implementation(project(":datastore"))
                implementation(project(":model"))
                implementation(project(":database"))
            }
        }

        val androidMain by getting {
            apply(plugin = "kotlin-parcelize")
            dependencies {
                implementation("androidx.appcompat:appcompat:1.5.1")
                implementation("androidx.core:core-ktx:1.9.0")
                implementation("androidx.activity:activity-ktx:1.6.1")
                implementation("androidx.activity:activity-compose:1.6.1")
                runtimeOnly("androidx.compose.material3:material3:1.0.1")
                implementation("androidx.multidex:multidex:2.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")
                implementation("io.coil-kt:coil-compose:2.2.2")
                implementation("io.coil-kt:coil-svg:2.2.2")
                implementation("com.google.accompanist:accompanist-pager:$accompanist")
                implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist")

                implementation("androidx.media3:media3-exoplayer:$exoplayer")
                implementation("androidx.media3:media3-exoplayer-dash:$exoplayer")
                implementation("androidx.media3:media3-exoplayer-hls:$exoplayer")
                implementation("androidx.media3:media3-exoplayer-rtsp:$exoplayer")
                implementation("androidx.media3:media3-exoplayer-rtsp:$exoplayer")
                implementation("androidx.media3:media3-exoplayer-smoothstreaming:$exoplayer")
                implementation("androidx.media3:media3-ui:$exoplayer")
            }
        }

        val desktopMain by getting {
            resources.srcDirs("src/desktopMain/resources", "src/commonMain/resources", "src/commonMain/assets")
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.github.pdvrieze.xmlutil:core-jvm:0.84.3")
                implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.84.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutines")
                implementation("com.sealwu:kscript-tools:1.0.21")
                implementation("net.harawata:appdirs:1.2.1")
                implementation("uk.co.caprica:vlcj:4.8.2")
            }
        }
    }
}

android {
    sourceSets["main"].setRoot("src/androidMain/")
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    sourceSets["main"].assets.srcDirs("src/androidMain/assets", "src/commonMain/assets")

    compileSdk = Configuration.compileSdk
    buildToolsVersion = Configuration.buildTools

    defaultConfig {
        applicationId = artifact
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = appCode
        versionName = appVersion

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = CompileOptions.sourceCompatibility
        targetCompatibility = CompileOptions.targetCompatibility
    }
}

aboutLibraries {
    includePlatform = true
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.GROUP
}

compose {
    kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.7.20"))
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.7.22")

    desktop {
        application {
            mainClass = "dev.datlag.burningseries.MainKt"

            nativeDistributions {
                targetFormats(
                    TargetFormat.AppImage, TargetFormat.Deb, TargetFormat.Rpm,
                    TargetFormat.Exe, TargetFormat.Msi,
                    TargetFormat.Dmg
                )

                packageName = "Burning-Series"
                packageVersion = appVersion
                outputBaseDir.set(rootProject.buildDir.resolve("release"))
                description = "Watch any series from Burning-Series using this (unofficial) app."
                copyright = "© 2020 Jeff Retz (DatLag). All rights reserved."
                licenseFile.set(rootProject.file("LICENSE"))

                linux {
                    iconFile.set(file("src/commonMain/assets/png/launcher_128.png"))
                    rpmLicenseType = "GPL-3.0"
                    debMaintainer = "Jeff Retz (DatLag)"
                    appCategory = "Video"
                }
                windows {
                    iconFile.set(file("src/commonMain/assets/ico/launcher_128.ico"))
                    upgradeUuid = "3487d337-1ef5-4e01-87cb-d1ede6e10752"
                }
                macOS {
                    iconFile.set(file("src/commonMain/assets/icns/launcher.icns"))
                }

                includeAllModules = true
            }
        }
    }
}

tasks.withType<Copy>().all {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
