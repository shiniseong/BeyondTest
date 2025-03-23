package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception

class AlreadyExistActivatedPrescriptionCode(code: String) : RuntimeException() {
    override val message: String = "이미 활성화된 처방 코드가 존재합니다. (activated code: $code)"
}