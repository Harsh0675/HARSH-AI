plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.harsh.ai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.harsh.ai"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"
        externalNativeBuild { cmake { cppFlags += "-std=c++17" } }
    }
    buildTypes { release { isMinifyEnabled = false } }
    externalNativeBuild { cmake { path = file("src/main/cpp/CMakeLists.txt") } }
    packaging { jniLibs { useLegacyPackaging = true } }
}

kotlin { jvmToolchain(17) }
