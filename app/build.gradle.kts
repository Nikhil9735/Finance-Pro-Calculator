plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.bibekarsoftwaretechnologies.FinancialProCalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bibekarsoftwaretechnologies.FinancialProCalculator"
        minSdk = 24
        targetSdk = 34
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
    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.sun.mail:android-mail:1.6.6");
    implementation ("com.sun.mail:android-activation:1.6.6");
    implementation("com.google.android.gms:play-services-ads:23.6.0");
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.0")
    implementation ("com.google.android.material:material:1.9.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}