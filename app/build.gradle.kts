plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.sensorapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sensorapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Mantenemos JavaVersion 11
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ConstraintLayout para layouts XML (formularios)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


    // --- NUEVAS DEPENDENCIAS AÑADIDAS ---

    // 1. Navigation Compose (para NavHost, rememberNavController, composable)
    implementation("androidx.navigation:navigation-compose:2.7.7") // Usa una versión reciente y estable

    // 2. OkHttp (para ApiService)
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // Usa una versión reciente

    // Para los íconos de Material Design
    implementation("androidx.compose.material:material-icons-extended:1.6.5")


    // --- FIN NUEVAS DEPENDENCIAS ---

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}