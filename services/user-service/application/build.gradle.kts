plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    api(project(":services:user-service:domain"))
    api(libs.slf4j)
    api(libs.logback.core)
    api(libs.logback.classic)
}
