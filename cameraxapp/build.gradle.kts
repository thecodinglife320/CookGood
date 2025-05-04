plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.google.hilt)
   alias(libs.plugins.google.ksp)
}

android {
   namespace = "com.example.cameraxapp"
   compileSdk = 35

   defaultConfig {
      applicationId = "com.example.cameraxapp"
      minSdk = 24
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
      viewBinding = true
   }
}

dependencies {

   implementation(libs.androidx.activity.compose)
   implementation(platform(libs.androidx.compose.bom))
   implementation(libs.androidx.hilt.navigation.compose)
   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)
   implementation(libs.androidx.material3)
   implementation(libs.androidx.activity)
   implementation(libs.androidx.constraintlayout)
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
   implementation(libs.androidx.camera.core)
   implementation(libs.androidx.camera.camera2)
   implementation(libs.androidx.camera.view)
   implementation(libs.androidx.camera.extensions)
   implementation(libs.accompanist.permissions)
   implementation(libs.androidx.camera.lifecycle)
   implementation(libs.coil.compose)

   //hilt
   implementation(libs.hilt.android)
   ksp(libs.hilt.android.compiler)
}