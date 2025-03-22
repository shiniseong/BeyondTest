package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception

class PrescriptionCodeNotFoundException(message: String) : DataNotFoundException(message) {
    companion object {
        private const val DEFAULT_MESSAGE = "처방 코드를 찾을 수 없습니다."
        fun default(code: String) = PrescriptionCodeNotFoundException("$DEFAULT_MESSAGE (code: $code)")
    }
}