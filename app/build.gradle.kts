import java.io.FileInputStream
import java.util.Properties

plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.kotlin.compose)
   alias(libs.plugins.google.ksp)
   alias(libs.plugins.google.hilt)
   alias(libs.plugins.google.gms.google.services)
}

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
   localProperties.load(FileInputStream(localPropertiesFile))
}

android {
   namespace = "com.ad.cookgood"
   compileSdk = 36

   defaultConfig {
      applicationId = "com.ad.cookgood"
      minSdk = 26
      targetSdk = 36
      versionCode = 1
      versionName = "1.0"
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

      // Read the API key and add it to BuildConfig
      buildConfigField("String", "APPWRITE_ENDPOINT",
         "\"${localProperties.getProperty("APPWRITE_ENDPOINT")}\""
      )
      buildConfigField("String", "APPWRITE_PROJECT_ID",
         "\"${localProperties.getProperty("APPWRITE_PROJECT_ID")}\""
      )
      buildConfigField("String", "APPWRITE_BUCKET_ID",
         "\"${localProperties.getProperty("APPWRITE_BUCKET_ID")}\""
      )
      buildConfigField("String", "GOOGLECLIENTID",
         "\"${localProperties.getProperty("GOOGLECLIENTID")}\""
      )
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
}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.lifecycle.runtime.ktx)
   implementation(libs.androidx.activity.compose)
   implementation(platform(libs.androidx.compose.bom))
   implementation(platform(libs.firebase.bom))
   implementation(libs.androidx.ui)
   implementation(libs.androidx.ui.graphics)
   implementation(libs.androidx.ui.tooling.preview)
   implementation(libs.androidx.material3)

   implementation(libs.accompanist.permissions)
   implementation(libs.androidx.ui.text.google.fonts)

   implementation(libs.exoplayer.core)
   implementation(libs.exoplayer.ui)

   implementation(libs.firebase.auth)
   implementation(libs.firebase.firestore)

   //room
   implementation(libs.bundles.room)
   ksp(libs.androidx.room.compiler)

   //hilt
   implementation(libs.bundles.hilt)
   ksp(libs.hilt.android.compiler)

   //camerax
   implementation(libs.bundles.camerax)

   //coil
   implementation(libs.bundles.coil)

   //google sign in
   implementation(libs.bundles.googleSignin)

   implementation(libs.androidx.navigation.compose)
   implementation(libs.material.icons.extended)
   implementation(libs.sdk.for1.android)

   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
   androidTestImplementation(platform(libs.androidx.compose.bom))
   androidTestImplementation(libs.androidx.ui.test.junit4)
   debugImplementation(libs.androidx.ui.tooling)
   debugImplementation(libs.androidx.ui.test.manifest)
}