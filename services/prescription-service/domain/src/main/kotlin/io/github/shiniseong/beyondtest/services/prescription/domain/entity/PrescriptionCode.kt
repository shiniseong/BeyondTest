package io.github.shiniseong.beyondtest.services.prescription.domain.entity

import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.github.shiniseong.beyondtest.services.prescription.domain.exception.InvalidPrescriptionCodeException
import io.github.shiniseong.beyondtest.services.prescription.domain.exception.InvalidPrescriptionStatusException
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.PrescriptionCodeValue
import io.github.shiniseong.beyondtest.shared.utils.now
import kotlinx.datetime.LocalDateTime

data class PrescriptionCode(
    val id: String,
    val code: PrescriptionCodeValue,
    val status: PrescriptionCodeStatus,
    val createdBy: String,
    val activatedFor: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val activatedAt: LocalDateTime? = null,
    val expiredAt: LocalDateTime? = null,
) {
    init {
        require(createdBy.isNotBlank()) { InvalidPrescriptionCodeException.createdByIsBlank() }
        if (status.isCreated()) {
            require(activatedFor == null) { InvalidPrescriptionCodeException.default() }
            require(activatedAt == null) { InvalidPrescriptionCodeException.default() }
            require(expiredAt == null) { InvalidPrescriptionCodeException.default() }
        }
    }

    infix fun activateFor(userId: String): PrescriptionCode {
        require(status.isCreated()) { InvalidPrescriptionStatusException.notValidToActivate(status) }

        return this.copy(
            status = PrescriptionCodeStatus.ACTIVATED,
            activatedFor = userId,
            activatedAt = LocalDateTime.now(),
        )
    }

    fun expired(): PrescriptionCode {
        require(status.isActivated()) { InvalidPrescriptionStatusException.notValidToExpire(status) }

        return this.copy(
            status = PrescriptionCodeStatus.EXPIRED,
            expiredAt = LocalDateTime.now(),
        )
    }

}