plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "ap.mobile.composablemap"
  compileSdk = 35

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.15"
  }

  defaultConfig {
    applicationId = "ap.mobile.composablemap"
    minSdk = 25
    targetSdk = 35
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
  implementation(libs.androidx.compose.material3)

  // implementation(libs.androidx.material.icons.extended)
  // implementation(libs.material3)
  // implementation(libs.maps.compose)
  // implementation(libs.navigation.compose)
  implementation("androidx.compose.material3:material3:1.3.1")
  implementation("androidx.compose.material:material-icons-extended:1.7.8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
  implementation("com.google.maps.android:maps-compose:6.4.1")
  implementation("com.google.maps.android:maps-compose-utils:6.4.1")
  implementation("androidx.navigation:navigation-compose:2.8.9")
  implementation("com.google.android.gms:play-services-maps:19.1.0")
  implementation("com.google.android.gms:play-services-location:21.3.0")
  implementation("com.jakewharton.timber:timber:5.0.1")
  implementation("androidx.compose.runtime:runtime:1.7.8")
  implementation("androidx.compose.runtime:runtime-livedata:1.7.8")
  implementation(libs.androidx.ui.text.google.fonts)
  implementation(libs.androidx.work.runtime.ktx)

  implementation("androidx.navigation:navigation-ui:2.8.9")
  implementation("androidx.navigation:navigation-compose:2.8.9")

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}