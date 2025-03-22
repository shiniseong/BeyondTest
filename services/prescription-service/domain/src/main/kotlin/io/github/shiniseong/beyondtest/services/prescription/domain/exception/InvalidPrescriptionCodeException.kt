package io.github.shiniseong.beyondtest.services.prescription.domain.exception

class InvalidPrescriptionCodeException(message: String) : IllegalStateException(message) {
    companion object {
        private const val DEFAULT_MESSAGE = "처방코드가 유효하지 않습니다."
        private const val CREATED_BY_IS_BLANK_MESSAGE = "처방 코드 발행 기관 정보가 공백 입니다."

        fun default(): InvalidPrescriptionCodeException {
            return InvalidPrescriptionCodeException(DEFAULT_MESSAGE)
        }

        fun createdByIsBlank(): InvalidPrescriptionCodeException {
            return InvalidPrescriptionCodeException(CREATED_BY_IS_BLANK_MESSAGE)
        }
    }
}