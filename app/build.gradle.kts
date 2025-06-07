plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.kotlin.compose)
   alias(libs.plugins.google.ksp)
   alias(libs.plugins.google.hilt)
   alias(libs.plugins.google.gms.google.services)
}

android {
   namespace = "com.ad.cookgood"
   compileSdk = 35

   defaultConfig {
      applicationId = "com.ad.cookgood"
      minSdk = 26
      targetSdk = 35
      versionCode = 1
      versionName = "1.0"
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      buildConfigField("String", "APPWRITE_ENDPOINT", "\"https://fra.cloud.appwrite.io/v1\"")
      buildConfigField("String", "APPWRITE_PROJECT_ID", "\"684170a5002413c6c4c8\"")
      buildConfigField("String", "APPWRITE_BUCKET_ID", "\"68417176002dac7c6a1e\"")
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
      buildConfig = true
   }
   ksp { // Nếu bạn đang sử dụng KSP
      arg("room.schemaLocation", "$project.directory/schemas".toString())
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
   implementation(libs.androidx.constraintlayout.compose)

   implementation(libs.androidx.room.ktx)
   implementation(libs.androidx.room.runtime)
   implementation(libs.androidx.hilt.navigation.compose)
   implementation(libs.androidx.camera.core)
   implementation(libs.androidx.camera.camera2)
   implementation(libs.accompanist.permissions)
   implementation(libs.androidx.camera.lifecycle)
   implementation(libs.coil.compose)
   implementation(libs.androidx.camera.compose)
   implementation(libs.firebase.auth)
   implementation(libs.androidx.credentials)
   implementation(libs.androidx.credentials.play.services.auth)
   implementation(libs.googleid)
   implementation(libs.androidx.ui.text.google.fonts)
   testImplementation(libs.mockito.core)
   androidTestImplementation(libs.core.ktx)
   ksp(libs.androidx.room.compiler)

   implementation(libs.hilt.android)
   ksp(libs.hilt.android.compiler)

   testImplementation(libs.junit)
   testImplementation(libs.kotlinx.coroutines.test)

   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
   androidTestImplementation(platform(libs.androidx.compose.bom))
   androidTestImplementation(libs.androidx.ui.test.junit4)
   androidTestImplementation(libs.androidx.runner)
   androidTestImplementation(libs.androidx.rules)
   androidTestImplementation(libs.androidx.room.testing)
   androidTestImplementation(libs.kotlinx.coroutines.test)

   debugImplementation(libs.androidx.ui.tooling)
   debugImplementation(libs.androidx.ui.test.manifest)

   implementation(libs.androidx.navigation.compose)
   implementation(libs.material3)
   implementation(libs.material.icons.extended)
   implementation(libs.sdk.for1.android)

}