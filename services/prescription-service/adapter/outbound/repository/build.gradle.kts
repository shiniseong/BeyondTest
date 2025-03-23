plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.springKotlin)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    api(project(":services:prescription-service:application"))

    dependencies {
        implementation(libs.spring.boot.starter.data.r2dbc)
        implementation(libs.reactor.kotlin.extensions)
        implementation(libs.kotlinx.coroutines.reactor)
        runtimeOnly(libs.r2dbc.mysql)
        runtimeOnly(libs.mysql.connector)
        testRuntimeOnly(libs.r2dbc.h2)
        testRuntimeOnly(libs.h2.database)
        testImplementation(libs.bundles.testEcosystem)
    }
}
