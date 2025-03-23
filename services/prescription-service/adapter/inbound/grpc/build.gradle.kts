plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.protobuf)
    alias(libs.plugins.springKotlin)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    api(project(":services:prescription-service:application"))

    implementation(libs.grpc.services)
    implementation(libs.spring.grpc.spring.boot.starter)

    testImplementation(libs.bundles.testEcosystem)
    testImplementation(libs.spring.grpc.test)
}
dependencyManagement {
    imports {
        mavenBom(libs.spring.grpc.dependencies.get().toString())
    }
}
