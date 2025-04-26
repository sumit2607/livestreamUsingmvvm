// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
buildscript {
    //extra["hilt_version"] = "2.50"  // Your version here
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${"2.50"}")
    }
}

