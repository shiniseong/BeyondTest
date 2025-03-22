package io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.mock

import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus

class PrescriptionCodeMockRepository : PrescriptionCodeRepositoryPort {
    private val prescriptionCodeMap = mutableMapOf<String, PrescriptionCode>()
    override fun insert(prescriptionCode: PrescriptionCode): PrescriptionCode {
        prescriptionCodeMap[prescriptionCode.code.value]
            ?.let { throw IllegalArgumentException("Prescription code already exists") }

        prescriptionCodeMap[prescriptionCode.code.value] = prescriptionCode
        return prescriptionCode
    }

    override fun update(prescriptionCode: PrescriptionCode): PrescriptionCode {
        prescriptionCodeMap[prescriptionCode.code.value]
            ?: throw IllegalArgumentException("Prescription code not found")

        prescriptionCodeMap[prescriptionCode.code.value] = prescriptionCode
        return prescriptionCode
    }

    override fun findByCode(codeValue: String): PrescriptionCode? {
        return prescriptionCodeMap[codeValue]
    }

    override fun findAllByUserIdAndStatus(userId: String, status: PrescriptionCodeStatus): List<PrescriptionCode> {
        return prescriptionCodeMap.values.filter { it.activatedFor == userId && it.status == status }
    }
}