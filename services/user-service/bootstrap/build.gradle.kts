plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.springKotlin)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":services:user-service:application"))
    implementation(project(":services:user-service:adapter:inbound:web"))
    implementation(project(":services:user-service:adapter:outbound:repository"))
    implementation(project(":services:user-service:adapter:outbound:grpc"))
    implementation(libs.springdoc.openapi.starter.webflux.ui)
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.grpc.dependencies.get().toString())
    }
}