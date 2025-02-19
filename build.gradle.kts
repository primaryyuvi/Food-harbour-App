buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath ("com.android.tools.build:gradle:8.6.0")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}