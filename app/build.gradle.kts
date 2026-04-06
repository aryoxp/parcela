plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  kotlin("plugin.serialization") version "2.1.0"
}

android {
  namespace = "ap.mobile.composablemap"
  compileSdk = 36

  buildFeatures {
    compose = true
  }

  defaultConfig {
    applicationId = "ap.mobile.composablemap"
    minSdk = 25
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

kotlin {
  compilerOptions {
    languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
    // jvmTarget value defaults to android.compileOptions.targetCompatibility
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
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.concurrent.futures.ktx)

  implementation("androidx.compose.material3:material3:1.4.0")
  implementation("androidx.compose.material:material-icons-extended:1.7.8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
  implementation("com.google.maps.android:maps-compose:8.2.2")
  implementation("com.google.maps.android:maps-compose-utils:8.2.2")
  implementation("androidx.navigation:navigation-compose:2.9.7")
  implementation("com.google.android.gms:play-services-maps:20.0.0")
  implementation("com.google.android.gms:play-services-location:21.3.0")
  implementation("com.jakewharton.timber:timber:5.0.1")
  implementation("androidx.compose.runtime:runtime:1.10.6")
  implementation("androidx.compose.runtime:runtime-livedata:1.10.6")
  implementation("androidx.compose.ui:ui-text-google-fonts:1.10.6")
  implementation("androidx.work:work-runtime-ktx:2.11.2")

  implementation("androidx.navigation:navigation-ui:2.9.7")
  implementation("androidx.navigation:navigation-compose:2.9.7")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

  implementation("androidx.datastore:datastore-preferences:1.2.1")

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
// kapt {
//   correctErrorTypes = true
// }