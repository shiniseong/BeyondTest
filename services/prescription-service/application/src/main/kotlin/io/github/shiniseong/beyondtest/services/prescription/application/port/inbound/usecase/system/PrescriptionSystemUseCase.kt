package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.system

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionSystemUseCase {
    fun expirePrescriptionCode(): List<PrescriptionCode>
}