import java.util.Properties

// --- 1. LOAD PROPERTIES ONCE (Top of file) ---
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

// --- 2. EXTRACT KEYS SAFELY ---
// If the key is missing in local.properties, it defaults to an empty string "" to prevent build crashes
val tmdbApiKey = localProperties.getProperty("tmdb.api.key") ?: ""
val razorpayKeyId = localProperties.getProperty("RAZORPAY_KEY_ID") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.ticket"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ticket"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- 3. INJECT KEYS INTO BUILD CONFIG ---
        // Note: We wrap the values in escaped quotes like "\"value\"" so they appear as Strings in Java/Kotlin
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
        buildConfigField("String", "RAZORPAY_KEY_ID", "\"$razorpayKeyId\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true // <--- IMPORTANT: This MUST be true
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.foundation.layout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    //ksp
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.4.0") // Use Coil if using Jetpack Compose
    //coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    // REQUIRED FOR GOOGLE SIGN IN (Credential Manager)
    implementation("androidx.credentials:credentials:1.2.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    //matrl3

    implementation("androidx.compose.material:material-icons-extended")
    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // For Flow/Coroutines
    ksp("androidx.room:room-compiler:$roomVersion") // Code Generator

    //firebae
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")

    //razorpay
    implementation("com.razorpay:checkout:1.6.33")


}