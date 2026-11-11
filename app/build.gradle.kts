
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.jarvis.telugu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jarvis.telugu"
        minSdk = 24
        targetSdk = 28
        versionCode = 1
        versionName = "1.0"
    }
}
