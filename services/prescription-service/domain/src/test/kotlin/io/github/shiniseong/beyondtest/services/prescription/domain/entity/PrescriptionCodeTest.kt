package io.github.shiniseong.beyondtest.services.prescription.domain.entity

import io.github.shiniseong.beyondtest.services.prescription.domain.exception.InvalidPrescriptionCodeException
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.toPrescriptionCodeValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class PrescriptionCodeTest : StringSpec({
    "PrescriptionCodeValue 생성" {
        // given
        // no given

        // when
        PrescriptionCode.generateCodeValue().toPrescriptionCodeValue()

        // then
        // no exception
    }

    "유효한 데이터로 처방 코드 생성" {
        // given
        val codeValue = PrescriptionCode.generateCodeValue()
        val hospitalId = "hospitalId"

        // when
        val prescriptionCode = PrescriptionCode.create(codeValue, hospitalId)

        // then
        prescriptionCode.activatedFor shouldBe null
        prescriptionCode.activatedAt shouldBe null
        prescriptionCode.expiredAt shouldBe null
    }

    "병원 아이디가 공백인 경우 예외를 발생시킨다." {
        // given
        val codeValue = PrescriptionCode.generateCodeValue()
        val hospitalId = "  "

        // when
        val exception = shouldThrow<InvalidPrescriptionCodeException> {
            PrescriptionCode.create(codeValue, hospitalId)
        }

        // then
        exception.message shouldBe InvalidPrescriptionCodeException.createdByIsBlank().message
    }

    "activateFor는 처방 코드를 활성화 하고 처방 코드 활성화 일로부터 6주 후를 만료 일로 설정 한다." {
        // given
        val codeValue = PrescriptionCode.generateCodeValue()
        val hospitalId = "hospitalId"
        val prescriptionCode = PrescriptionCode.create(codeValue, hospitalId)
        val activatedFor = "user123"
        val fixedLocalDateTime = LocalDateTime(2025, 3, 23, 10, 10)
        val fixedInstant = fixedLocalDateTime.toInstant(TimeZone.currentSystemDefault())
        mockkObject(Clock.System)
        every { Clock.System.now() } returns fixedInstant

        // when
        val result = prescriptionCode.activateFor(activatedFor)

        // then
        val expectedActivatedAt = fixedLocalDateTime
        val expectedExpiredAt = LocalDateTime(
            2025,
            5,
            4,
            23,
            59,
            59,
            999_999_999
        )
        result.activatedFor shouldBe activatedFor
        result.activatedAt shouldBe expectedActivatedAt
        result.expiredAt shouldBe expectedExpiredAt
    }
})