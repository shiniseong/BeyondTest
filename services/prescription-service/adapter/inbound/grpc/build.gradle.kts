plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    api(project(":services:prescription-service:application"))

    dependencies {

        testImplementation(libs.bundles.testEcosystem)
    }
}
