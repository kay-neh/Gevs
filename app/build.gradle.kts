plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gevs"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gevs"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Any Chart
    implementation("com.github.AnyChart:AnyChart-Android:1.1.5")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    // Import the Firebase BoM
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")
    // Add the dependency for the Realtime Database library
    implementation("com.google.firebase:firebase-database")
    // Add the dependency for the Cloud Storage library
    implementation("com.google.firebase:firebase-storage")
    // Add the dependency for the Cloud Messaging library
    implementation("com.google.firebase:firebase-messaging")

    // define any required OkHttp artifacts without version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.1"))
    implementation("com.squareup.okhttp3:okhttp")

    // scanner implementation
    implementation ("com.google.android.gms:play-services-vision:20.1.3")


}