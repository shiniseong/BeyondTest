package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception

class InvalidGetPrescriptionCodeByUserIdAndStatusQueryException(message: String) : RuntimeException(message) {
    companion object {
        const val DEFAULT_MESSAGE = "유효하지 않은 사용자 ID 및 상태로 처방전을 조회하려고 시도했습니다."
        fun default() = InvalidGetPrescriptionCodeByUserIdAndStatusQueryException(DEFAULT_MESSAGE)
    }
}
