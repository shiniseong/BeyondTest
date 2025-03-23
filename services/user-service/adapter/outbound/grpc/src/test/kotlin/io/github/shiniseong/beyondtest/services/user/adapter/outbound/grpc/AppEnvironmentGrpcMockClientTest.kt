package io.github.shiniseong.beyondtest.services.user.adapter.outbound.grpc

import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query.GetLatestAppEnvironmentQuery
import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.vo.toAppVersion
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class AppEnvironmentGrpcMockClientTest : StringSpec({
    val client = AppEnvironmentGrpcMockClient()

    "Android RELEASE 환경은 올바른 데이터를 반환해야 한다" {
        // given
        val query = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.RELEASE)

        // when
        val response = client.getLatestAppEnvironment(query)

        // then
        response.latestVersion shouldBe "1.2.3".toAppVersion()
        response.minimumRequiredVersion shouldBe "1.0.0".toAppVersion()
        response.validHashes shouldContain "android-release-hash"
    }

    "Android DEBUG 환경은 올바른 데이터를 반환해야 한다" {
        // given
        val query = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.DEBUG)

        // when
        val response = client.getLatestAppEnvironment(query)

        // then
        response.latestVersion shouldBe "1.2.3-alpha.1".toAppVersion()
        response.minimumRequiredVersion shouldBe "1.0.0-alpha.3".toAppVersion()
        response.validHashes shouldContain "android-debug-hash"
        response.validHashes shouldContain "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
    }

    "iOS RELEASE 환경은 올바른 데이터를 반환해야 한다" {
        // given
        val query = GetLatestAppEnvironmentQuery(OS.IOS, BuildMode.RELEASE)

        // when
        val response = client.getLatestAppEnvironment(query)

        // then
        response.latestVersion shouldBe "1.2.3".toAppVersion()
        response.minimumRequiredVersion shouldBe "1.0.0".toAppVersion()
        response.validHashes shouldContain "ios-release-hash"
    }

    "iOS DEBUG 환경은 올바른 데이터를 반환해야 한다" {
        // given
        val query = GetLatestAppEnvironmentQuery(OS.IOS, BuildMode.DEBUG)

        // when
        val response = client.getLatestAppEnvironment(query)

        // then
        response.latestVersion shouldBe "1.2.3-alpha.1".toAppVersion()
        response.minimumRequiredVersion shouldBe "1.0.0-alpha.3".toAppVersion()
        response.validHashes shouldContain "ios-debug-hash"
    }

    "Android DEBUG 환경은 여러 유효한 해시를 포함해야 한다" {
        // given
        val query = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.DEBUG)

        // when
        val response = client.getLatestAppEnvironment(query)

        // then
        response.validHashes.size shouldBe 2
        response.validHashes.containsAll(setOf("android-debug-hash", "Y95ULTuEF0uXNq7fSNa1EEzP0FU=")) shouldBe true
    }

    "각 OS와 모드 조합은 고유한 환경을 가져야 한다" {
        // given
        val androidReleaseQuery = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.RELEASE)
        val androidDebugQuery = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.DEBUG)
        val iosReleaseQuery = GetLatestAppEnvironmentQuery(OS.IOS, BuildMode.RELEASE)
        val iosDebugQuery = GetLatestAppEnvironmentQuery(OS.IOS, BuildMode.DEBUG)

        // when
        val androidReleaseResponse = client.getLatestAppEnvironment(androidReleaseQuery)
        val androidDebugResponse = client.getLatestAppEnvironment(androidDebugQuery)
        val iosReleaseResponse = client.getLatestAppEnvironment(iosReleaseQuery)
        val iosDebugResponse = client.getLatestAppEnvironment(iosDebugQuery)

        // then
        // Verify that valid hashes are unique per environment
        (androidReleaseResponse.validHashes intersect androidDebugResponse.validHashes).isEmpty() shouldBe true
        (androidReleaseResponse.validHashes intersect iosReleaseResponse.validHashes).isEmpty() shouldBe true
        (androidReleaseResponse.validHashes intersect iosDebugResponse.validHashes).isEmpty() shouldBe true
        (androidDebugResponse.validHashes intersect iosReleaseResponse.validHashes).isEmpty() shouldBe true
        (androidDebugResponse.validHashes intersect iosDebugResponse.validHashes).isEmpty() shouldBe true
        (iosReleaseResponse.validHashes intersect iosDebugResponse.validHashes).isEmpty() shouldBe true
    }

    "각 플랫폼별 해시값이 올바르게 매핑되어야 한다" {
        // given
        val androidDebugQuery = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.DEBUG)
        val androidReleaseQuery = GetLatestAppEnvironmentQuery(OS.ANDROID, BuildMode.RELEASE)
        val iosDebugQuery = GetLatestAppEnvironmentQuery(OS.IOS, BuildMode.DEBUG)
        val iosReleaseQuery = GetLatestAppEnvironmentQuery(OS.IOS, BuildMode.RELEASE)

        // when
        val androidDebugResult = client.getLatestAppEnvironment(androidDebugQuery)
        val androidReleaseResult = client.getLatestAppEnvironment(androidReleaseQuery)
        val iosDebugResult = client.getLatestAppEnvironment(iosDebugQuery)
        val iosReleaseResult = client.getLatestAppEnvironment(iosReleaseQuery)

        // then
        androidDebugResult.validHashes.size shouldBe 2
        androidReleaseResult.validHashes.size shouldBe 1
        iosDebugResult.validHashes.size shouldBe 1
        iosReleaseResult.validHashes.size shouldBe 1

        // 특정 해시값이 올바른 OS/Mode 조합에만 포함되어 있는지 확인
        androidDebugResult.validHashes shouldContain "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
        androidDebugResult.validHashes shouldContain "android-debug-hash"
        androidReleaseResult.validHashes shouldContain "android-release-hash"
        iosDebugResult.validHashes shouldContain "ios-debug-hash"
        iosReleaseResult.validHashes shouldContain "ios-release-hash"
    }
})