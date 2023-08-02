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

@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    extra["agp_version"] = "8.1.0"
    extra["kotlin_version"] = "1.9.0"
    extra["ksp_version"] = "${extra["kotlin_version"]}-1.0.12"
    extra["compose_compiler_version"] = "1.5.1"
    extra["room_version"] = "2.5.2"
    extra["hilt_version"] = "2.47"
}

plugins {
    id("com.android.test") version "${project.extra["agp_version"]}" apply false
    id("com.android.application") version "${project.extra["agp_version"]}" apply false
    id("com.android.library") version "${project.extra["agp_version"]}" apply false
    kotlin("android") version "${project.extra["kotlin_version"]}" apply false
    id("com.google.devtools.ksp") version "${project.extra["ksp_version"]}" apply false
    id("com.google.dagger.hilt.android") version "${project.extra["hilt_version"]}" apply false
}
