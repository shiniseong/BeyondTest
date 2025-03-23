package io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus

interface PrescriptionCodeRepositoryPort {
    suspend fun insert(prescriptionCode: PrescriptionCode): PrescriptionCode
    suspend fun update(prescriptionCode: PrescriptionCode): PrescriptionCode
    suspend fun findByCode(codeValue: String): PrescriptionCode?
    suspend fun findAllByUserIdAndStatus(userId: String, status: PrescriptionCodeStatus): List<PrescriptionCode>
}