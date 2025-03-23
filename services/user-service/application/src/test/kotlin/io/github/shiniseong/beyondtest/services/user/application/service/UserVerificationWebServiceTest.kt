package io.github.shiniseong.beyondtest.services.user.application.service

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command.VerifyUserCommand
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.AppEnvironmentClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query.GetLatestAppEnvironmentQuery
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.response.AppEnvironmentResponse
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.prescriptioncode.PrescriptionCodeClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.repository.UserVerificationHistoryRepositoryPort
import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.enums.UpdateType
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class UserVerificationWebServiceTest : StringSpec({
    // Mock dependencies
    val appEnvironmentClient = mockk<AppEnvironmentClientPort>()
    val prescriptionCodeClient = mockk<PrescriptionCodeClientPort>()
    val userVerificationHistoryRepository = mockk<UserVerificationHistoryRepositoryPort>(relaxed = true)

    // Create service
    val service = UserVerificationWebService(
        appEnvironmentClient,
        prescriptionCodeClient,
        userVerificationHistoryRepository
    )

    beforeTest {
        clearAllMocks()
    }

    "최신 버전이고 모든 검증이 통과될 경우 업데이트 타입은 NONE이어야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("1.0.0")
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0")
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns true

        // when
        val result = service.verifyUser(command)

        // then
        result.updateType shouldBe UpdateType.NONE
        result.shouldUpdate shouldBe false

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
            userVerificationHistoryRepository.insert(any())
        }
    }

    "앱 버전이 최소 요구 버전 이상이지만 최신 버전보다 낮을 경우 업데이트 타입은 RECOMMENDED여야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("0.9.5")
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0")
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns true

        // when
        val result = service.verifyUser(command)

        // then
        result.updateType shouldBe UpdateType.RECOMMENDED
        result.shouldUpdate shouldBe true

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
            userVerificationHistoryRepository.insert(any())
        }
    }

    "앱 버전이 최소 요구 버전보다 낮을 경우 업데이트 타입은 REQUIRED여야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("0.8.0")
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0")
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns true

        // when
        val result = service.verifyUser(command)

        // then
        result.updateType shouldBe UpdateType.REQUIRED
        result.shouldUpdate shouldBe true

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
            userVerificationHistoryRepository.insert(any())
        }
    }

    "해시값이 유효하지 않을 경우 예외가 발생해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("1.0.0")
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "InvalidHash"

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0")
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf("ValidHash")

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        // when & then
        val exception = shouldThrow<IllegalArgumentException> {
            service.verifyUser(command)
        }

        exception.message shouldBe "App 검증에 실패했습니다. Hash값이 유효하지 않습니다."

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            userVerificationHistoryRepository.insert(any())
        }

        coVerify(exactly = 0) {
            prescriptionCodeClient.verifyPrescriptionCodeFor(any())
        }
    }

    "활성화된 처방코드가 없을 경우 예외가 발생해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("1.0.0")
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0")
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns false

        // when & then
        val exception = shouldThrow<IllegalArgumentException> {
            service.verifyUser(command)
        }

        exception.message shouldBe "활성화 된 처방 코드가 없습니다."

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
            userVerificationHistoryRepository.insert(any())
        }
    }

    "다양한 버전 형식으로도 정상 동작해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("0.1.2-alpha.1")
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("0.1.2")
        val minimumRequiredVersion = AppVersion.parse("0.1.1")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns true

        // when
        val result = service.verifyUser(command)

        // then
        result.updateType shouldBe UpdateType.RECOMMENDED
        result.shouldUpdate shouldBe true

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
            userVerificationHistoryRepository.insert(any())
        }
    }

    "iOS 앱에 대한 검증도 정상 작동해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("1.0.0")
        val os = OS.IOS
        val mode = BuildMode.RELEASE
        val hash = "iOSReleaseHash123"

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0")
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns true

        // when
        val result = service.verifyUser(command)

        // then
        result.updateType shouldBe UpdateType.NONE

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        }
    }
    
    "최신 버전 보다 더 높은 버전으로 검증 요청이 온 경우 예외를 발생시켜야 한다." {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val version = AppVersion.parse("2.0.0") // 최신 버전(1.0.0)보다 높은 버전
        val os = OS.ANDROID
        val mode = BuildMode.DEBUG
        val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

        val command = VerifyUserCommand(
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash
        )

        val latestVersion = AppVersion.parse("1.0.0") // 서버에서 알고 있는 최신 버전
        val minimumRequiredVersion = AppVersion.parse("0.9.0")
        val validHashes = setOf(hash)

        val appEnvironmentResponse = AppEnvironmentResponse(
            latestVersion = latestVersion,
            minimumRequiredVersion = minimumRequiredVersion,
            validHashes = validHashes
        )

        coEvery {
            appEnvironmentClient.getLatestAppEnvironment(any())
        } returns appEnvironmentResponse

        // 처방코드 검증도 모킹해야 함 (실제 코드에서 버전 검증 전에 처방코드 검증이 수행됨)
        coEvery {
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId)
        } returns true

        // when & then
        val exception = shouldThrow<IllegalArgumentException> {
            service.verifyUser(command)
        }

        exception.message shouldBe "알 수 없는 버전입니다. 최신 버전보다 더 높은 버전입니다. (request: 2.0.0, latest: 1.0.0)"

        coVerify(exactly = 1) {
            appEnvironmentClient.getLatestAppEnvironment(GetLatestAppEnvironmentQuery(os, mode))
            prescriptionCodeClient.verifyPrescriptionCodeFor(userId) // 처방코드 검증이 호출되었는지 확인
            userVerificationHistoryRepository.insert(any())
        }
    }
})