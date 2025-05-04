import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.nirvaysofttech.FinancialProCalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nirvaysofttech.FinancialProCalculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"

        // Add build date as a string resource
        resValue("string", "build_date", SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date()))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += setOf("META-INF/NOTICE.md", "META-INF/LICENSE.md")
        }
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
    implementation ("com.android.billingclient:billing:6.1.0");
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