package io.github.shiniseong.beyondtest.services.prescription.domain.entity

import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.github.shiniseong.beyondtest.services.prescription.domain.exception.InvalidPrescriptionCodeException
import io.github.shiniseong.beyondtest.services.prescription.domain.exception.InvalidPrescriptionStatusException
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.PrescriptionCodeValue
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.toPrescriptionCodeValue
import io.github.shiniseong.beyondtest.shared.utils.endOfDay
import io.github.shiniseong.beyondtest.shared.utils.now
import io.github.shiniseong.beyondtest.shared.utils.plusWeeks
import kotlinx.datetime.LocalDateTime

data class PrescriptionCode(
    val code: PrescriptionCodeValue,
    val status: PrescriptionCodeStatus,
    val createdBy: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val activatedFor: String? = null,
    val activatedAt: LocalDateTime? = null,
    val expiredAt: LocalDateTime? = null,
) {
    init {
        require(createdBy.isNotBlank()) { throw InvalidPrescriptionCodeException.createdByIsBlank() }
        if (status.isCreated()) {
            require(activatedFor == null) { throw InvalidPrescriptionCodeException.default() }
            require(activatedAt == null) { throw InvalidPrescriptionCodeException.default() }
            require(expiredAt == null) { throw InvalidPrescriptionCodeException.default() }
        }
    }

    infix fun activateFor(userId: String): PrescriptionCode {
        require(status.isCreated()) { InvalidPrescriptionStatusException.notValidToActivate(status) }

        val activatedAt = LocalDateTime.now()
        return this.copy(
            status = PrescriptionCodeStatus.ACTIVATED,
            activatedFor = userId,
            activatedAt = activatedAt,
            expiredAt = activatedAt.toExpiredAt()

        )
    }

    fun expired(): PrescriptionCode {
        require(status.isActivated()) { InvalidPrescriptionStatusException.notValidToExpire(status) }

        return this.copy(
            status = PrescriptionCodeStatus.EXPIRED,
        )
    }

    private fun LocalDateTime.toExpiredAt(): LocalDateTime =
        this.plusWeeks(6).endOfDay()

    companion object {
        fun create(code: String, hospitalId: String) = PrescriptionCode(
            code = code.toPrescriptionCodeValue(),
            status = PrescriptionCodeStatus.CREATED,
            createdBy = hospitalId,
        )

        fun generateCodeValue(): String {
            // 4개의 랜덤 대문자 알파벳을 생성합니다.
            val letters = List(4) { ('A'..'Z').random() }
            val digits = List(4) { ('0'..'9').random() }

            return (letters + digits)
                .shuffled()
                .joinToString("")
        }
    }
}