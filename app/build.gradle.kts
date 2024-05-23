/*
 * Copyright 2022 Erfan Sn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(Configs.JVM_TOOLCHAIN_VERSION)
}

android {
    compileSdk = Configs.COMPILE_SDK_VERSION
    namespace = Configs.PACKAGE_NAME

    defaultConfig {
        applicationId = Configs.PACKAGE_NAME
        minSdk = Configs.MIN_SDK_VERSION
        targetSdk = Configs.TARGET_SDK_VERSION
        versionCode = 3
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        benchmark {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += "release"
            proguardFiles("benchmark-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}", "DebugProbesKt.bin")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.profileinstaller)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.mathparser.org.mxparser)
    implementation(libs.kotlinx.datetime)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.material)
    implementation(libs.material.icons.extended)
    implementation(libs.ui.tooling.preview)

    implementation(libs.activity.compose)
    implementation(libs.constraintlayout.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    testImplementation(composeBom)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent.jvm)
    testImplementation(libs.robolectric)
    testImplementation(libs.ext.junit)
    testImplementation(libs.ui.test.junit4)

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    ksp(libs.room.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
