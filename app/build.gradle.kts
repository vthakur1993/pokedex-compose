plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.hilt)
    id("kotlin-parcelize")
    id("androidx.room") version libs.versions.room
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10" // Use the same version as your Kotlin plugin
}

android {
    namespace = "com.example.pokedex"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pokedex"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.2"
        testInstrumentationRunner = "com.example.pokedex.CustomTestRunner"
        testInstrumentationRunnerArguments.putAll( mutableMapOf(
            "clearPackageData" to "true",
            "dagger.hilt.android.internal.testing.TestApplicationComponentManager.HiltTestApplication" to "dagger.hilt.android.testing.HiltTestApplication"
        ))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    }
    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
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
    implementation(libs.androidx.palette)
    testImplementation(libs.junit)
    testImplementation(libs.room.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.mockito)
    androidTestImplementation(libs.robolectric)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigationCompose)
    implementation(libs.androidx.hilt)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.coil)
    implementation(libs.coil.network)
    implementation(libs.google.gson)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.retrofit.moshi.codegen)

}