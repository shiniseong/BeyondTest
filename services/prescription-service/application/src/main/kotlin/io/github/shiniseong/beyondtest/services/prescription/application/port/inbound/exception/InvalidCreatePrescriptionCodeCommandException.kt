package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception

class InvalidCreatePrescriptionCodeCommandException(message: String) : RuntimeException(message) {
    companion object {
        const val DEFAULT_MESSAGE = "유효하지 않은 처방 코드 생성 요청입니다."
        fun default() = InvalidCreatePrescriptionCodeCommandException(DEFAULT_MESSAGE)
    }
}
