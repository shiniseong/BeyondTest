import com.google.protobuf.gradle.id

plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.protobuf)
    alias(libs.plugins.springDependencyManagement)
    id(libs.plugins.javaTestFixtures.get().toString())
}

dependencies {
    api(project(":services:prescription-service:application"))

    implementation(libs.grpc.services)
    implementation(libs.spring.grpc.spring.boot.starter)

    testImplementation(libs.bundles.testEcosystem)
    testImplementation(libs.spring.grpc.test)
    testImplementation("io.grpc:grpc-testing:1.54.1")
    testImplementation(testFixtures(project(":services:prescription-service:application")))
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.grpc.dependencies.get().toString())
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("jakarta_omit")
                    option("@generated=omit")
                }
            }
        }
    }
}
