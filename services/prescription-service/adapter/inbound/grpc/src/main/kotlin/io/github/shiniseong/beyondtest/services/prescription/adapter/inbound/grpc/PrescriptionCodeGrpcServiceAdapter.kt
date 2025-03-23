package io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.PrescriptionCodeGrpcUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.query.FindPrescriptionCodeByUserIdAndStatusQuery
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class PrescriptionCodeGrpcServiceAdapter(
    private val prescriptionCodeGrpcUseCase: PrescriptionCodeGrpcUseCase
) : PrescriptionCodeServiceGrpc.PrescriptionCodeServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PrescriptionCodeGrpcServiceAdapter::class.java)

    override fun findAllByUserIdAndStatus(
        request: FindPrescriptionCodeByUserIdAndStatusRequest,
        responseObserver: StreamObserver<FindPrescriptionCodeByUserIdAndStatusResponse>
    ) {
        runBlocking {
            try {
                logger.info("gRPC request received: findAllPrescriptionCodeByUserIdAndStatus for userId=${request.userId}, status=${request.status}")

                val query = FindPrescriptionCodeByUserIdAndStatusQuery(
                    userId = request.userId,
                    status = PrescriptionCodeStatus.valueOf(request.status)
                )

                val prescriptionCodes = prescriptionCodeGrpcUseCase.findAllPrescriptionCodeByUserIdAndStatus(query)

                val response = FindPrescriptionCodeByUserIdAndStatusResponse.newBuilder()
                    .addAllPrescriptionCodes(prescriptionCodes.map { it.toProto() })
                    .build()

                responseObserver.onNext(response)
                responseObserver.onCompleted()

                logger.info("gRPC response sent: ${prescriptionCodes.size} prescription codes found")
            } catch (e: Exception) {
                logger.error("Error in gRPC findAllPrescriptionCodeByUserIdAndStatus", e)
                responseObserver.onError(e)
            }
        }
    }

    /**
     * PrescriptionCode 도메인 객체를 Proto 객체로 변환합니다.
     */
    private fun PrescriptionCode.toProto(): PrescriptionCodeProto {
        val builder = PrescriptionCodeProto.newBuilder()
            .setCode(this.code.value)
            .setStatus(this.status.name)
            .setCreatedBy(this.createdBy)
            .setCreatedAt(this.createdAt.toString())

        this.activatedFor?.let { builder.setActivatedFor(it) }
        this.activatedAt?.let { builder.setActivatedAt(it.toString()) }
        this.expiredAt?.let { builder.setExpiredAt(it.toString()) }

        return builder.build()
    }
}