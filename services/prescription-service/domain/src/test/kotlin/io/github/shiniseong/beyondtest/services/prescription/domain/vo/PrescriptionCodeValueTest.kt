package io.github.shiniseong.beyondtest.services.prescription.domain.vo

import io.github.shiniseong.beyondtest.services.prescription.domain.exception.NotValidPrescriptionCodeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PrescriptionCodeValueTest : StringSpec({
    "PrescriptionCodeValue 생성시 길이가 8글자가 아니면 NotValidPrescriptionCodeException이 발생한다." {
        // given
        val code = "1234567"
        // when
        val exception = shouldThrow<NotValidPrescriptionCodeException> {
            PrescriptionCodeValue(code)
        }
        // then
        exception.message shouldBe NotValidPrescriptionCodeException.NOT_VALID_LENGTH_MESSAGE
    }
    "PrescriptionCodeValue 생성시 숫자4글자, 영문 대문자 4글자가 아니면 NotValidPrescriptionCodeException이 발생한다." {
        // given
        val code = "1111aaaa"
        // when
        val exception = shouldThrow<NotValidPrescriptionCodeException> {
            PrescriptionCodeValue(code)
        }
        // then
        exception.message shouldBe NotValidPrescriptionCodeException.notValidFormat(code)
    }
    "PrescriptionCodeValue 생성시 숫자4글자, 영문 대문자 4글자로 생성된다." {
        // given
        val code = "1A2B3C4D"
        // when
        val prescriptionCodeValue = PrescriptionCodeValue(code)
        // then
        prescriptionCodeValue.value shouldBe code
    }
})