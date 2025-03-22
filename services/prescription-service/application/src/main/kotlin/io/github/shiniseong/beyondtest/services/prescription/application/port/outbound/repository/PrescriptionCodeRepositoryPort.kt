package io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus

interface PrescriptionCodeRepositoryPort {
    fun insert(prescriptionCode: PrescriptionCode): PrescriptionCode
    fun update(prescriptionCode: PrescriptionCode): PrescriptionCode
    fun findByCode(codeValue: String): PrescriptionCode?
    fun findAllByUserIdAndStatus(userId: String, status: PrescriptionCodeStatus): List<PrescriptionCode>
}