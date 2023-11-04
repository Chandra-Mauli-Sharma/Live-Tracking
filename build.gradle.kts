// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.playServices) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.realm) apply false
//    kotlin("plugin.serialization") version "1.8.10"
    alias(libs.plugins.serializationKotlin) apply false
    alias(libs.plugins.mapPlatformSecret)  apply false
}

true // Needed to make the Suppress annotation work for the plugins block