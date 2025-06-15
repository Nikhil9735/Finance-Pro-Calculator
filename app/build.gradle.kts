import java.text.SimpleDateFormat
import java.util.*
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
}

val buildInfoFile = rootProject.file("build_info.properties")
val buildInfoProps = Properties()

if (buildInfoFile.exists()) {
    buildInfoFile.inputStream().use { buildInfoProps.load(it) }
}

val currentVersionCode = 3 // 🔁 Change this when releasing a new version
val previousVersionCode = buildInfoProps.getProperty("versionCode")?.toIntOrNull()
var buildDate = buildInfoProps.getProperty("buildDate") ?: ""

if (currentVersionCode != previousVersionCode) {
    buildDate = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
    buildInfoProps["versionCode"] = currentVersionCode.toString()
    buildInfoProps["buildDate"] = buildDate
    buildInfoFile.outputStream().use { buildInfoProps.store(it, null) }
}

android {
    namespace = "com.nirvaysofttech.FinancePro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nirvaysofttech.FinancePro"
        minSdk = 24
        targetSdk = 34
        versionCode = currentVersionCode
        versionName = "0.0.$currentVersionCode"

        // Set build date from stored value
        resValue("string", "build_date", buildDate)

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
    implementation("com.android.billingclient:billing:7.1.1")
    implementation("com.sun.mail:android-mail:1.6.6")
    implementation("com.sun.mail:android-activation:1.6.6")
    implementation("com.google.android.gms:play-services-ads:24.4.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.1")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
