plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.springKotlin)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":services:prescription-service:application"))
    implementation(project(":services:prescription-service:adapter:inbound:web"))
    implementation(project(":services:prescription-service:adapter:inbound:grpc"))
    implementation(project(":services:prescription-service:adapter:outbound:repository"))
    implementation(libs.springdoc.openapi.starter.webflux.ui)
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.grpc.dependencies.get().toString())
    }
}