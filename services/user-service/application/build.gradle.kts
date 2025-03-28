plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    id(libs.plugins.javaTestFixtures.get().toString())
}

dependencies {
    api(project(":services:user-service:domain"))
    testImplementation(libs.bundles.testEcosystem)
}
