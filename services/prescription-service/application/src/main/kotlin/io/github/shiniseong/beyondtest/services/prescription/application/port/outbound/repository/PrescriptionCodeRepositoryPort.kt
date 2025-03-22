package io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository

import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.query.FindPrescriptionCodeByCodeValueQuery
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.query.FindPrescriptionCodeByUserIdAndStatusQuery
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionCodeRepositoryPort {
    fun insertPrescriptionCode(prescriptionCode: PrescriptionCode): PrescriptionCode
    fun updatePrescriptionCode(prescriptionCode: PrescriptionCode): PrescriptionCode
    fun findPrescriptionCodeByCodeValue(query: FindPrescriptionCodeByCodeValueQuery): PrescriptionCode?
    fun findPrescriptionCodeByUserIdAndStatus(query: FindPrescriptionCodeByUserIdAndStatusQuery): List<PrescriptionCode>
}