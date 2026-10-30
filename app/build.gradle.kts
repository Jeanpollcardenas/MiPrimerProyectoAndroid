plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.miprimerproyectoandroid"
    compileSdk = 36

    bundle {
        language {
            enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        applicationId = "com.example.miprimerproyectoandroid"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    androidResources {
        localeFilters += listOf("es", "en", "fr", "de")
    }
}
