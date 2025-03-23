package io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.web.controller.api

import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.mock.PrescriptionCodeMockRepository
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.service.PrescriptionCodeWebService
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.kotest.core.spec.style.StringSpec
import org.springframework.test.web.reactive.server.WebTestClient

class PrescriptionCodeRestControllerTest : StringSpec({
    val repository = PrescriptionCodeMockRepository()
    val service = PrescriptionCodeWebService(repository)
    val controller = PrescriptionCodeRestController(service)

    val webTestClient = WebTestClient
        .bindToRouterFunction(controller)
        .build()

    beforeTest {
        repository.clearAll()
    }

    "처방 코드 생성 API가 정상적으로 동작해야 한다." {
        // given
        val hospitalId = "hospitalId123"
        val requestCommand = CreatePrescriptionCodeCommand(hospitalId)

        // when & then
        webTestClient.post()
            .uri("/api/v1/prescription-codes")
            .bodyValue(requestCommand)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.createdBy").isEqualTo(hospitalId)
            .jsonPath("$.status").isEqualTo("CREATED")
    }

    "처방 코드 활성화 API가 정상적으로 동작해야 한다." {
        // given
        val userId = "userId123"
        val createdCode = "ABCD1234"
        val hospitalId = "hospitalId123"

        val prescriptionCode = PrescriptionCode.create(code = createdCode, hospitalId = hospitalId)
        repository.insert(prescriptionCode)

        val requestCommand = ActivatePrescriptionCodeCommand(userId = userId, code = createdCode)

        // when & then
        webTestClient.patch()
            .uri("/api/v1/prescription-codes/one/status/activated")
            .bodyValue(requestCommand)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.activatedFor").isEqualTo(userId)
            .jsonPath("$.status").isEqualTo("ACTIVATED")
    }
})