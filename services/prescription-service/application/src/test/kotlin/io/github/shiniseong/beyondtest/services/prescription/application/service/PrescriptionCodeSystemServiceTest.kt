package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.mock.PrescriptionCodeMockRepository
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.shared.utils.endOfDay
import io.github.shiniseong.beyondtest.shared.utils.plusWeeks
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class PrescriptionCodeSystemServiceTest : StringSpec({
    val repository = PrescriptionCodeMockRepository()
    val service = PrescriptionCodeSystemService(repository)
    "expirePrescriptionCode() 함수는 상태가 활성화 되어 있으면서 만료일이 오늘보다 이전인 처방 코드를 만료 처리한다." {
        // given
        val code1 = "ABCD1234"
        val code2 = "EFGH5678"
        val code3 = "IJKL9012"
        val user1 = "user1"
        val user2 = "user2"
        val hospitalId = "hospitalId123"
        val createdAt = LocalDateTime(2025, 2, 10, 5, 0, 0)
        val fixedInstant = createdAt.toInstant(TimeZone.currentSystemDefault())
        val nowAt = createdAt.plusWeeks(6).endOfDay()
        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedInstant
        val prescriptionCode1 = PrescriptionCode.create(code = code1, hospitalId = hospitalId)
        val prescriptionCode2 = PrescriptionCode.create(code = code2, hospitalId = hospitalId)
        val prescriptionCode3 = PrescriptionCode.create(code = code3, hospitalId = hospitalId)

        repository.insert(prescriptionCode1)
        repository.insert(prescriptionCode2)
        repository.insert(prescriptionCode3)

        val activatedPrescriptionCode1 = prescriptionCode1.activateFor(user1)
        val activatedPrescriptionCode2 = prescriptionCode2.activateFor(user2)

        repository.update(activatedPrescriptionCode1)
        repository.update(activatedPrescriptionCode2)

        every { Clock.System.now() } returns nowAt.toInstant(TimeZone.currentSystemDefault())

        // when
        val result = service.expirePrescriptionCode()

        // then
        result.size shouldBe 2
        result.map { it.code.value } shouldBe listOf("ABCD1234", "EFGH5678")
        result.all { it.status.isExpired() } shouldBe true
    }

})