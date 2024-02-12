// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
    id("androidx.room") version "2.6.0" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        // 其他仓库...
    }
    dependencies {


        // 其他classpath依赖...
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}