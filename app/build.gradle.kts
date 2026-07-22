plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.greninja.frogdownloader"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.greninja.frogdownloader"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // Eliminamos x86 y x86_64 para reducir drásticamente el peso (son para emuladores)
            // Mantenemos solo arquitecturas de teléfonos reales (32 y 64 bits)
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a"))
        }

        // Solo incluir recursos para español e inglés
        androidResources {
            localeFilters.addAll(setOf("es", "en"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Activa R8 para eliminar código no usado
            isShrinkResources = false // Elimina recursos (imágenes, xml) no usados
            ndk {
                debugSymbolLevel = "none" // Elimina símbolos de depuración de las librerías nativas (FFmpeg, etc)
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
        resources {
            pickFirsts += "lib/*/libc++_shared.so"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("androidx.navigation:navigation-compose:2.8.8")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.github.junkfood02.youtubedl-android:library:0.18.1")
    implementation("io.github.junkfood02.youtubedl-android:ffmpeg:0.18.1")
    implementation("androidx.media3:media3-exoplayer:1.10.1") // O la versión estable que estés usando
    implementation("androidx.media3:media3-exoplayer-dash:1.10.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.10.1")
    implementation("androidx.media3:media3-ui:1.10.1")
    implementation("androidx.media3:media3-session:1.10.1") // Usa el mismo número de versión que tus otros media3
}