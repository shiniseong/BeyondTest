package io.github.shiniseong.beyondtest.services.prescription.domain.vo

import io.github.shiniseong.beyondtest.services.prescription.domain.exception.NotValidPrescriptionCodeException

private const val PRESCRIPTION_CODE_LENGTH = 8
private const val PRESCRIPTION_CODE_UPPERCASE_COUNT = 4
private const val PRESCRIPTION_CODE_DIGIT_COUNT = 4

@JvmInline
value class PrescriptionCodeValue(val value: String) {
    init {
        require(value.length == PRESCRIPTION_CODE_LENGTH) {
            throw NotValidPrescriptionCodeException.notValidLength(this.value)
        }
        require(this.isValidFormat()) {
            throw NotValidPrescriptionCodeException.notValidFormat(this.value)
        }
    }
}

private fun PrescriptionCodeValue.isValidFormat(): Boolean {
    var upperCaseCount = 0
    var digitCount = 0
    value.forEach { char ->
        when {
            char.isUpperCase() -> upperCaseCount++
            char.isDigit() -> digitCount++
            // 대문자나 숫자 외의 문자가 포함되어 있으면 검증 실패 처리
            else -> return false
        }
    }
    return upperCaseCount == PRESCRIPTION_CODE_UPPERCASE_COUNT && digitCount == PRESCRIPTION_CODE_DIGIT_COUNT
}