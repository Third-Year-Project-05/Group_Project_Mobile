import com.android.tools.r8.internal.de
import org.gradle.internal.impldep.com.fasterxml.jackson.core.JsonPointer.compile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.echolynk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.echolynk"
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

    buildFeatures{
        viewBinding = true
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
    implementation(libs.firebase.firestore)
    implementation (libs.firebase.ui.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.recaptchabase)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Induwara





    // Dakshina
    implementation(libs.firebase.auth)
    implementation(libs.google.firebase.auth)
    implementation(libs.play.services.auth)
    implementation (libs.firebase.auth.v2101)
    implementation (libs.okhttp)
    implementation (libs.volley)
    implementation (libs.stripe.android.v2120)
    //implementation ("com.stripe:stripe-android:20.27.0")






    // Sandaru

    implementation(libs.roundimage)
    implementation(libs.lottie)
    implementation(libs.imagepicker)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)






    // Wikum







    // Lumbini







    // Theekshana


}