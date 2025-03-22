package io.github.shiniseong.beyondtest.services.prescription.domain.exception

import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionStatus


/**
 * 처방전 상태가 유효하지 않을 때 발생하는 예외
 */
class InvalidPrescriptionStatusException(message: String, status: PrescriptionStatus) :
    IllegalStateException("$message (status: $status)") {
    companion object {
        private const val NOT_VALID_TO_ACTIVATE_MESSAGE = "이미 활성화 되었거나 만료된 처방 코드 입니다."

        fun notValidToActivate(status: PrescriptionStatus) =
            InvalidPrescriptionStatusException(NOT_VALID_TO_ACTIVATE_MESSAGE, status)

        fun notValidToExpire(status: PrescriptionStatus) =
            InvalidPrescriptionStatusException("이미 만료 되었거나 활성상태가 아닌 처방 코드 입니다.", status)
    }
}