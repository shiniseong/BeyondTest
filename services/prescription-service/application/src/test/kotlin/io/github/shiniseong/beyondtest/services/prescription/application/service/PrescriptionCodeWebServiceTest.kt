package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.AlreadyExistActivatedPrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.PrescriptionCodeNotFoundException
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.PrescriptionCodeValue
import io.github.shiniseong.beyondtest.shared.utils.endOfDay
import io.github.shiniseong.beyondtest.shared.utils.now
import io.github.shiniseong.beyondtest.shared.utils.plusWeeks
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PrescriptionCodeWebServiceTest : StringSpec({
    val repository = mockk<PrescriptionCodeRepositoryPort>()
    val service = PrescriptionCodeWebService(repository)

    beforeTest {
        clearAllMocks()
    }

    "생성된 처방 코드가 중복되지 않고 한번에 생성되는 경우" {
        // given
        val hospitalId = "hospitalId123"
        val command = CreatePrescriptionCodeCommand(hospitalId = hospitalId)

        // mock the code generator
        val uniqueCode = "ABCD1234"
        mockkObject(PrescriptionCode)
        every { PrescriptionCode.generateCodeValue() } returns uniqueCode

        // mock the repository behavior
        coEvery { repository.findByCode(uniqueCode) } returns null
        coEvery { repository.insert(any()) } answers { firstArg() }
        // when
        val result = service.createPrescriptionCode(command)

        // then
        result.code.value shouldBe uniqueCode
        result.createdBy shouldBe hospitalId
        result.status shouldBe PrescriptionCodeStatus.CREATED

        coVerify(exactly = 1) {
            repository.findByCode(uniqueCode)
            repository.insert(any())
        }

        unmockkObject(PrescriptionCode)
    }

    "생성된 처방 코드가 중복되어 재생성 후 저장 되는 경우 " {
        // given
        val hospitalId = "hospitalId123"
        val command = CreatePrescriptionCodeCommand(hospitalId = hospitalId)

        // mock the code generator
        val duplicateCode = "ABCD1234"
        val uniqueCode = "EFGH5678"
        mockkObject(PrescriptionCode)
        every { PrescriptionCode.generateCodeValue() } returnsMany listOf(duplicateCode, uniqueCode)

        // mock the repository behavior
        val existingCode = mockk<PrescriptionCode>()
        coEvery { repository.findByCode(duplicateCode) } returns existingCode
        coEvery { repository.findByCode(uniqueCode) } returns null
        coEvery { repository.insert(any()) } answers { firstArg() }

        // when
        val result = service.createPrescriptionCode(command)

        // then
        result.code.value shouldBe uniqueCode
        result.createdBy shouldBe hospitalId
        result.status shouldBe PrescriptionCodeStatus.CREATED

        coVerify(exactly = 1) {
            repository.findByCode(duplicateCode)
            repository.findByCode(uniqueCode)
            repository.insert(any())
        }
    }

    "처방 코드 활성화시 처방 코드가 존재하지 않는 경우" {
        // given
        val codeString = "ABCD1234"
        val userId = "userId123"
        val command = ActivatePrescriptionCodeCommand(userId = userId, code = codeString)

        // mock the repository behavior
        coEvery { repository.findByCode(codeString) } returns null

        // when & then
        val result = shouldThrow<PrescriptionCodeNotFoundException> { service.activatePrescriptionCode(command) }

        result.message shouldBe PrescriptionCodeNotFoundException.default(codeString).message

        coVerify(exactly = 1) {
            repository.findByCode(codeString)
        }
    }

    "처방 코드 활성화시 이미 활성화 된 처방 코드가 존재하는 경우" {
        // given
        val codeString = "ABCD1234"
        val activatedCodeString = "EFGH5678"
        val userId = "userId123"
        val command = ActivatePrescriptionCodeCommand(userId = userId, code = codeString)
        val existingPrescriptionCode = PrescriptionCode(
            code = PrescriptionCodeValue(codeString),
            status = PrescriptionCodeStatus.ACTIVATED,
            createdBy = "hospitalId123",
            activatedFor = userId,
            createdAt = LocalDateTime.now(),
            activatedAt = LocalDateTime.now(),
            expiredAt = null
        )
        val alreadyActivatedCode = PrescriptionCode(
            code = PrescriptionCodeValue(activatedCodeString),
            status = PrescriptionCodeStatus.ACTIVATED,
            createdBy = "hospitalId123",
            activatedFor = userId,
            createdAt = LocalDateTime.now(),
            activatedAt = LocalDateTime.now(),
            expiredAt = null
        )

        // mock the repository behavior
        coEvery { repository.findByCode(codeString) } returns existingPrescriptionCode
        coEvery { repository.findAllByUserIdAndStatus(any(), any()) } returns listOf(alreadyActivatedCode)

        // when & then
        val result = shouldThrow<AlreadyExistActivatedPrescriptionCode> { service.activatePrescriptionCode(command) }

        result.message shouldBe AlreadyExistActivatedPrescriptionCode(code = activatedCodeString).message

        coVerify(exactly = 1) {
            repository.findByCode(codeString)
            repository.findAllByUserIdAndStatus(userId, PrescriptionCodeStatus.ACTIVATED)
        }
    }

    "처방 코드 활성화 성공" {
        // given
        val codeString = "ABCD1234"
        val userId = "userId123"
        val command = ActivatePrescriptionCodeCommand(userId = userId, code = codeString)
        val hospitalId = "hospitalId123"
        val createdAt = LocalDateTime.now()
        val existingPrescriptionCode = PrescriptionCode(
            code = PrescriptionCodeValue(codeString),
            status = PrescriptionCodeStatus.CREATED,
            createdBy = hospitalId,
            activatedFor = null,
            createdAt = createdAt,
            activatedAt = null,
            expiredAt = null
        )

        // mock the repository behavior
        coEvery { repository.findByCode(codeString) } returns existingPrescriptionCode
        coEvery { repository.update(any()) } answers { firstArg() }
        coEvery {
            repository.findAllByUserIdAndStatus(userId, PrescriptionCodeStatus.ACTIVATED)
        } returns emptyList()

        // mock the activatedAt
        val fixedInstant = Clock.System.now()
        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedInstant

        // when
        val result = service.activatePrescriptionCode(command)

        // then
        result.code shouldBe existingPrescriptionCode.code
        result.status shouldBe PrescriptionCodeStatus.ACTIVATED
        result.createdBy shouldBe existingPrescriptionCode.createdBy
        result.activatedFor shouldBe userId
        result.createdAt shouldBe existingPrescriptionCode.createdAt
        result.activatedAt shouldBe fixedInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        (result.expiredAt shouldBe
                fixedInstant
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .plusWeeks(6)
                    .endOfDay())

        coVerify(exactly = 1) {
            repository.findByCode(codeString)
            repository.update(any())
        }
    }
})