// Top-level build file where you can add configuration options common to all subprojects/modules.
// Plugin versions are defined in the version catalog (libs.versions.toml).
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
