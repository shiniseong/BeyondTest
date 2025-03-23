package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.mock.PrescriptionCodeMockRepository
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.query.FindPrescriptionCodeByUserIdAndStatusQuery
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks

class PrescriptionCodeGrpcServiceTest : StringSpec({
    val repository = PrescriptionCodeMockRepository()
    val service = PrescriptionCodeGrpcService(repository)

    beforeTest {
        clearAllMocks()
    }

    "userId(activatedFor)와 status로 조회" {
        // given
        val userId = "userId123"
        val status = PrescriptionCodeStatus.ACTIVATED
        val code1 = "ABCD1234"
        val code2 = "EFGH5678"
        val code3 = "IJKL9012"
        val hospitalId = "hospitalId123"
        val prescriptionCode1 = PrescriptionCode.create(code = code1, hospitalId = hospitalId)
        val prescriptionCode2 = PrescriptionCode.create(code = code2, hospitalId = hospitalId)
        val prescriptionCode3 = PrescriptionCode.create(code = code3, hospitalId = hospitalId)

        repository.insert(prescriptionCode1)
        repository.insert(prescriptionCode2)
        repository.insert(prescriptionCode3)

        repository.update(prescriptionCode1.activateFor(userId))
        repository.update(prescriptionCode2.activateFor(userId))

        val query = FindPrescriptionCodeByUserIdAndStatusQuery(userId, status)

        // when
        val result = service.findAllPrescriptionCodeByUserIdAndStatus(query)

        // then
        result.size shouldBe 2
        result.map { it.code.value } shouldBe listOf("ABCD1234", "EFGH5678")
        result.all { it.status.isActivated() } shouldBe true
        result.all { it.activatedFor == userId } shouldBe true
    }
})