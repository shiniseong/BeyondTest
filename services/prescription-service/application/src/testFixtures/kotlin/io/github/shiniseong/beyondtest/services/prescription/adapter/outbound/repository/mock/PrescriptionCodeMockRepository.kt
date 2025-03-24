package io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.mock

import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import kotlinx.datetime.LocalDateTime

class PrescriptionCodeMockRepository : PrescriptionCodeRepositoryPort {
    private val prescriptionCodeMap = mutableMapOf<String, PrescriptionCode>()
    override suspend fun insert(prescriptionCode: PrescriptionCode): PrescriptionCode {
        prescriptionCodeMap[prescriptionCode.code.value]
            ?.let { throw IllegalArgumentException("Prescription code already exists") }

        prescriptionCodeMap[prescriptionCode.code.value] = prescriptionCode
        return prescriptionCode
    }

    override suspend fun update(prescriptionCode: PrescriptionCode): PrescriptionCode {
        prescriptionCodeMap[prescriptionCode.code.value]
            ?: throw IllegalArgumentException("Prescription code not found")

        prescriptionCodeMap[prescriptionCode.code.value] = prescriptionCode
        return prescriptionCode
    }

    override suspend fun updateAll(prescriptionCodes: List<PrescriptionCode>): List<PrescriptionCode> {
        prescriptionCodes.forEach { update(it) }
        return prescriptionCodes
    }

    override suspend fun findByCode(codeValue: String): PrescriptionCode? {
        return prescriptionCodeMap[codeValue]
    }

    override suspend fun findAll(): List<PrescriptionCode> {
        return prescriptionCodeMap.values.toList()
    }

    override suspend fun findAllByUserIdAndStatus(
        userId: String,
        status: PrescriptionCodeStatus
    ): List<PrescriptionCode> {
        return prescriptionCodeMap.values.filter { it.activatedFor == userId && it.status == status }
    }

    override suspend fun findAllToExpireFrom(from: LocalDateTime): List<PrescriptionCode> {
        return prescriptionCodeMap.values.filter {
            (it.status.isActivated()) && (it.expiredAt != null) && (it.expiredAt!! <= from)
        }
    }

    fun clearAll() {
        prescriptionCodeMap.clear()
    }
}