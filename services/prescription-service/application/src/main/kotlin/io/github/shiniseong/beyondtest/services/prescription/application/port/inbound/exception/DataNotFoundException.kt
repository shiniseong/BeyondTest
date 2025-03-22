package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception

open class DataNotFoundException(message: String) : RuntimeException(message) {
    companion object {
        const val DEFAULT_MESSAGE = "데이터를 찾을 수 없습니다."
        fun default() = DataNotFoundException(DEFAULT_MESSAGE)
    }
}