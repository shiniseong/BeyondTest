package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.system

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionCodeSystemUseCase {
    fun expirePrescriptionCode(): List<PrescriptionCode>
}