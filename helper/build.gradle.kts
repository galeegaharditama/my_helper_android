import de.fayard.refreshVersions.core.versionFor

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
}

group = "com.github.galeegaharditama"

android {
  compileSdk = AppConfig.compileSdk

  defaultConfig {
    minSdk = AppConfig.minSdk
    targetSdk = AppConfig.targetSdk

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug")
    getByName("release")
    create("debugcompose")
    create("releasecompose")
  }
  compileOptions {
    sourceCompatibility(JavaVersion.VERSION_1_8)
    targetCompatibility(JavaVersion.VERSION_1_8)
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
    // Enables Jetpack Compose for this module
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
  }
}

dependencies {
  api(Libs.timber)
  api(Libs.koin_android)
  api(Libs.retrofit)
  api(Libs.logging_interceptor)
  api(Libs.moshi_kotlin)
  api(Libs.converter_moshi)
  kapt(Libs.moshi_kotlin_codegen)

  implementation(Libs.lifecycle_runtime_ktx)
  implementation(Libs.lifecycle_viewmodel_compose)

  implementation(Libs.navigation_fragment_ktx)
  implementation(Libs.compressor)
  implementation(Libs.customactivityoncrash)

  implementation(Libs.constraintlayout)
  api(Libs.material)

  implementation(Libs.room_runtime)
  implementation(Libs.room_ktx)
  kapt(Libs.room_compiler)

  testImplementation(Libs.junit_junit)
  testImplementation(Libs.mockk)
  androidTestImplementation(Libs.androidx_test_ext_junit)
  androidTestImplementation(Libs.espresso_core)
}
