package io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import kotlinx.datetime.LocalDateTime

interface PrescriptionCodeRepositoryPort {
    suspend fun insert(prescriptionCode: PrescriptionCode): PrescriptionCode
    suspend fun update(prescriptionCode: PrescriptionCode): PrescriptionCode
    suspend fun updateAll(prescriptionCodes: List<PrescriptionCode>): List<PrescriptionCode>
    suspend fun findByCode(codeValue: String): PrescriptionCode?
    suspend fun findAll(): List<PrescriptionCode>
    suspend fun findAllByUserIdAndStatus(userId: String, status: PrescriptionCodeStatus): List<PrescriptionCode>
    suspend fun findAllToExpireFrom(from: LocalDateTime): List<PrescriptionCode>
}