package io.github.shiniseong.beyondtest.services.prescription.domain.enums

/**
 *
 */
enum class PrescriptionCodeStatus {
    CREATED,
    ACTIVATED,
    EXPIRED,
    ;

    fun isCreated(): Boolean = this == CREATED
    fun isActivated(): Boolean = this == ACTIVATED
}
typealias PrescriptionCodeStatusName = String

fun PrescriptionCodeStatusName.toPrescriptionCodeStatus(): PrescriptionCodeStatus = PrescriptionCodeStatus.valueOf(this)