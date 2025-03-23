package io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.prescriptioncode

interface PrescriptionCodeClientPort {
    fun verifyPrescriptionCodeFor(userId: String): Boolean
}