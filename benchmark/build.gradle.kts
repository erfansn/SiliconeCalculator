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
    id("com.android.test")
    kotlin("android")
}

kotlin {
    jvmToolchain(Configs.JVM_TOOLCHAIN_VERSION)
}

android {
    compileSdk = Configs.COMPILE_SDK_VERSION
    namespace = "${Configs.PACKAGE_NAME}.benchmark"

    defaultConfig {
        minSdk = Configs.MIN_SDK_VERSION.coerceAtLeast(23)
        targetSdk = Configs.TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It's signed with a debug key
        // for easy local/CI testing.
        benchmark {
            signingConfig = getByName("debug").signingConfig
            isDebuggable = true
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test:runner:1.5.2")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.benchmark:benchmark-macro-junit4:1.1.1")
}

androidComponents {
    beforeVariants {
        it.enable = it.buildType == "benchmark"
    }
}