package io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.query

import io.github.shiniseong.beyondtest.services.prescription.domain.vo.PrescriptionCodeValue

data class FindPrescriptionCodeByCodeValueQuery(val codeValue: PrescriptionCodeValue)
