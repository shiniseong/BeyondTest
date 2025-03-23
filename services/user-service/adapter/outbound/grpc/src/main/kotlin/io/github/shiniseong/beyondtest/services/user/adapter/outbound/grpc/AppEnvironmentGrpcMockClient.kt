package io.github.shiniseong.beyondtest.services.user.adapter.outbound.grpc

import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.AppEnvironmentClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query.GetLatestAppEnvironmentQuery
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.response.AppEnvironmentResponse
import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.vo.toAppVersion

class AppEnvironmentGrpcMockClient : AppEnvironmentClientPort {
    private val validHashMap = validHashes.groupBy { it.os to it.mode }
    private val androidReleaseLatestEnvironment = AppEnvironmentResponse(
        latestVersion = "1.2.3".toAppVersion(),
        minimumRequiredVersion = "1.0.0".toAppVersion(),
        validHashes = findValidHashes(OS.ANDROID, BuildMode.RELEASE)
    )
    private val iosReleaseLatestEnvironment = AppEnvironmentResponse(
        latestVersion = "1.2.3".toAppVersion(),
        minimumRequiredVersion = "1.0.0".toAppVersion(),
        validHashes = findValidHashes(OS.IOS, BuildMode.RELEASE)
    )
    private val androidDebugLatestEnvironment = AppEnvironmentResponse(
        latestVersion = "1.2.3-alpha.1".toAppVersion(),
        minimumRequiredVersion = "1.0.0-alpha.3".toAppVersion(),
        validHashes = findValidHashes(OS.ANDROID, BuildMode.DEBUG)
    )

    private val iosDebugLatestEnvironment = AppEnvironmentResponse(
        latestVersion = "1.2.3-alpha.1".toAppVersion(),
        minimumRequiredVersion = "1.0.0-alpha.3".toAppVersion(),
        validHashes = findValidHashes(OS.IOS, BuildMode.DEBUG)
    )

    override fun getLatestAppEnvironment(query: GetLatestAppEnvironmentQuery): AppEnvironmentResponse {
        return when (query.os) {
            OS.ANDROID -> when (query.mode) {
                BuildMode.RELEASE -> androidReleaseLatestEnvironment
                BuildMode.DEBUG -> androidDebugLatestEnvironment
            }

            OS.IOS -> when (query.mode) {
                BuildMode.RELEASE -> iosReleaseLatestEnvironment
                BuildMode.DEBUG -> iosDebugLatestEnvironment
            }
        }
    }

    private fun findValidHashes(os: OS, mode: BuildMode): Set<String> {
        return validHashMap[os to mode]?.map { it.hash }?.toSet() ?: emptySet()
    }
}

private val validHashes = listOf(
    OsToModeToHash(OS.ANDROID, BuildMode.DEBUG, "android-debug-hash"),
    OsToModeToHash(OS.ANDROID, BuildMode.RELEASE, "android-release-hash"),
    OsToModeToHash(OS.IOS, BuildMode.DEBUG, "ios-debug-hash"),
    OsToModeToHash(OS.IOS, BuildMode.RELEASE, "ios-release-hash"),
    OsToModeToHash(OS.ANDROID, BuildMode.DEBUG, "Y95ULTuEF0uXNq7fSNa1EEzP0FU=")
)

data class OsToModeToHash(
    val os: OS,
    val mode: BuildMode,
    val hash: String
)