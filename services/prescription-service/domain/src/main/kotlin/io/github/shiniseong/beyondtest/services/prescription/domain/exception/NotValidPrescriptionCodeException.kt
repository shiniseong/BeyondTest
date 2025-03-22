package io.github.shiniseong.beyondtest.services.prescription.domain.exception

class NotValidPrescriptionCodeException(message: String, code: String) :
    IllegalArgumentException("$message (code: $code)") {
    companion object {
        const val NOT_VALID_LENGTH_MESSAGE = "처방 코드는 8자여야 합니다."
        const val NOT_VALID_FORMAT_MESSAGE = "처방 코드는 영문 대문자 4글자와 숫자 4글자로 이루어져야 합니다."
        fun notValidLength(code: String) = NotValidPrescriptionCodeException(NOT_VALID_LENGTH_MESSAGE, code)
        fun notValidFormat(code: String) = NotValidPrescriptionCodeException(NOT_VALID_FORMAT_MESSAGE, code)
    }
}