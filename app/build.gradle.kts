import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ktlint)
}

val versionMajor = 1
val versionMinor = 3
val versionPatch = 1
val versionBuild = 1

android {
    namespace = "com.muhammadwahyudin.identitasku"
    compileSdk = 35

    signingConfigs {
        getByName("debug") {
        }
        create("release") {
            val props = Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }
            keyAlias = props.getProperty("keyAlias")
            keyPassword = props.getProperty("keyPassword")
            storeFile = file(props.getProperty("storeFile"))
            storePassword = props.getProperty("storePassword")
        }
    }

    defaultConfig {
        applicationId = "com.muhammadwahyudin.identitasku"
        minSdk = 26
        targetSdk = 35
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch.$versionBuild"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        applicationVariants.all {
            outputs.all {
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName = "identitasku_${versionName}.apk"
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            manifestPlaceholders["appName"] = "IdentitasKu-dev"
            manifestPlaceholders["crashlyticsEnabled"] = true
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appName"] = "IdentitasKu"
            manifestPlaceholders["crashlyticsEnabled"] = true
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    packaging {
        resources {
            excludes.add("META-INF/atomicfu.kotlin_module")
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.kotlin.stdlib.jdk7)
    // Android Jetpack
    implementation(libs.bundles.jetpack)
    // Coroutine
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.debug)
    debugImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.compose.ui.test.junit)

    // Lifecycle Arch
    implementation(libs.bundles.lifecycle)

    // DI
    implementation(libs.koin.android)
    // Firebase
    implementation(libs.bundles.firebase)

    // Chrome custom tab
    implementation(libs.androidx.browser)
    // Image
    implementation(libs.coil)
    // Language
    implementation(libs.lingver)
    // Log
    implementation(libs.timber)
    // Pref & Storage
    implementation(libs.hawk)
    implementation(libs.sqlcipher.android)
    // Process
    implementation(libs.process.phoenix)
    // Custom View
    implementation(libs.base.recyclerview.adapter.helper)
    implementation(libs.lottie)
    implementation(libs.creditcardview)
    implementation(libs.vvalidator)
    // ShowcaseView
    implementation(libs.material.intro.view)
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.espresso.core)
}

ktlint {
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    disabledRules.set(setOf("final-newline", "no-wildcard-imports", "import-ordering"))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}