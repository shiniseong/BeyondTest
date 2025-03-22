package io.github.shiniseong.beyondtest.services.prescription.domain.entity

import io.github.shiniseong.beyondtest.services.prescription.domain.exception.InvalidPrescriptionCodeException
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.toPrescriptionCodeValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

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
})