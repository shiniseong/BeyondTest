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
    api(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.reactor.kotlin.extensions)
    implementation(libs.kotlinx.coroutines.reactor)
//    runtimeOnly(libs.r2dbc.mysql)
//    runtimeOnly(libs.mysql.connector)
    api(libs.r2dbc.h2)
    runtimeOnly(libs.h2.database)
    testImplementation(libs.r2dbc.h2)
    testRuntimeOnly(libs.h2.database)
    testImplementation(libs.bundles.testEcosystem)
}

// bootJar 태스크 비활성화 (어댑터 모듈은 독립 실행 가능한 JAR가 필요 없음)
tasks.bootJar {
    enabled = false
}

// 일반 jar 태스크 활성화 (다른 모듈에서 의존성으로 사용하기 위함)
tasks.jar {
    enabled = true
}