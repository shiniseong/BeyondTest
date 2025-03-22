package io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.query

import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus

data class FindPrescriptionCodeByUserIdAndStatusQuery(val userId: String, val status: PrescriptionCodeStatus)
