// The settings file is the entry point of every Gradle build.
// Its primary purpose is to define the subprojects.
// It is also used for some aspects of project-wide configuration, like managing plugins, dependencies, etc.
// https://docs.gradle.org/current/userguide/settings_file_basics.html

dependencyResolutionManagement {
    // Use Maven Central as the default repository (where Gradle will download dependencies) in all subprojects.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":app")
include(":shared:utils")

// === Services ===
// User Service
include(":services:user-service")
include(":services:user-service:domain")
include(":services:user-service:application")
include(":services:user-service:adapter:inbound:rest")
include(":services:user-service:adapter:inbound:grpc")
include(":services:user-service:adapter:outbound:repository")
include(":services:user-service:adapter:outbound:grpc")
include(":services:user-service:bootstrap")
// Prescription Service
include(":services:prescription-service")
include(":services:prescription-service:domain")
include(":services:prescription-service:application")
include(":services:prescription-service:adapter:inbound:rest")
include(":services:prescription-service:adapter:inbound:grpc")
include(":services:prescription-service:adapter:outbound:repository")
include(":services:prescription-service:adapter:outbound:grpc")
include(":services:prescription-service:bootstrap")
// Notification Service
include(":services:notification-service")
include(":services:notification-service:domain")
include(":services:notification-service:application")
include(":services:notification-service:adapter:inbound:rest")
include(":services:notification-service:adapter:inbound:grpc")
include(":services:notification-service:adapter:outbound:repository")
include(":services:notification-service:adapter:outbound:grpc")
include(":services:notification-service:bootstrap")

rootProject.name = "beyondtest"