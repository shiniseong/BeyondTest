package io.github.shiniseong.beyondtest.services.user.adapter.inbound.web.controller.api

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.UserVerificationWebUseCase
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command.VerifyUserCommand
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.response.VerifyUserResponse
import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.enums.UpdateType
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import io.kotest.core.spec.style.StringSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

class UserVerificationRestControllerTest : StringSpec({
    // Mock the service
    val userVerificationService = mockk<UserVerificationWebUseCase>()

    // Create the controller with the mocked service
    val controller = UserVerificationRestController(userVerificationService)

    // Create WebTestClient
    val webTestClient = WebTestClient
        .bindToRouterFunction(controller)
        .build()

    beforeTest {
        clearAllMocks()
    }

    "사용자 검증 API는 명령을 받아 검증 결과를 반환해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val command = VerifyUserCommand(
            userId = userId,
            version = AppVersion.parse("1.0.0"),
            os = OS.ANDROID,
            mode = BuildMode.DEBUG,
            hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
        )
        val response = VerifyUserResponse(updateType = UpdateType.NONE)

        coEvery { userVerificationService.verifyUser(any()) } returns response

        // when & then
        webTestClient.post()
            .uri("/api/v1/users/verification")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isOk
            .expectBody<VerifyUserResponse>()
            .isEqualTo(response)
    }

    "사용자 버전이 낮을 때 업데이트 권장 응답을 반환해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val command = VerifyUserCommand(
            userId = userId,
            version = AppVersion.parse("0.9.5"),
            os = OS.ANDROID,
            mode = BuildMode.DEBUG,
            hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
        )
        val response = VerifyUserResponse(updateType = UpdateType.RECOMMENDED)

        coEvery { userVerificationService.verifyUser(command) } returns response

        // when & then
        webTestClient.post()
            .uri("/api/v1/users/verification")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isOk
            .expectBody<VerifyUserResponse>()
            .isEqualTo(response)
    }

    "사용자 버전이 최소 요구 버전보다 낮을 때 업데이트 강제 응답을 반환해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val command = VerifyUserCommand(
            userId = userId,
            version = AppVersion.parse("0.8.0"),
            os = OS.ANDROID,
            mode = BuildMode.DEBUG,
            hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
        )
        val response = VerifyUserResponse(updateType = UpdateType.REQUIRED)

        coEvery { userVerificationService.verifyUser(command) } returns response

        // when & then
        webTestClient.post()
            .uri("/api/v1/users/verification")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(command)
            .exchange()
            .expectStatus().isOk
            .expectBody<VerifyUserResponse>()
            .isEqualTo(response)
    }
})