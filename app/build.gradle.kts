plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.store"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.store"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Navigation
    implementation(libs.androidx.navigation.navigation.fragment.ktx9)
    implementation(libs.androidx.navigation.navigation.ui.ktx11)
    //Dagger - Hilt
    implementation(libs.com.google.dagger.hilt.android12)
    kapt(libs.com.google.dagger.hilt.compiler14)
    //DataStore
    implementation(libs.androidx.datastore.datastore.preferences30)
    //Retrofit
    implementation(libs.com.squareup.retrofit2.retrofit14)
    implementation(libs.com.squareup.retrofit2.converter.gson14)
    //OkHTTP client
    implementation(libs.com.squareup.okhttp3.okhttp17)
    implementation(libs.com.squareup.okhttp3.logging.interceptor17)
    //Coroutines
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core20)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android19)
    //Lifecycle
    implementation(libs.androidx.lifecycle.lifecycle.extensions23)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx25)
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.ktx.v261.x21)
    implementation(libs.androidx.lifecycle.lifecycle.livedata.ktx25)
    //Image Loading
    implementation(libs.coil)
    //Gson
    implementation(libs.google.gson)
    //Calligraphy
    implementation(libs.io.github.inflationx.calligraphy33)
    implementation(libs.io.github.inflationx.viewpump3)
    //Glide
    implementation(libs.github.glide)
    //Receive OTP
    implementation(libs.gms.play.services.base)
    implementation(libs.gms.play.services.auth.api.phone)
    //Other
    implementation(libs.shimmer)
    implementation(libs.shimmer.recyclerview)
    implementation(libs.lottie)
    implementation(libs.dynamicsizes)
    implementation(libs.pinview)
    implementation(libs.circleindicator)
    implementation(libs.readmore.textview)
    implementation(libs.mpandroidchart)
    implementation(libs.persian.date.picker.dialog)
    implementation(libs.ssimagepicker.v23)
    implementation(libs.carouselrecyclerview)
}
