package io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc

import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.mock.PrescriptionCodeMockRepository
import io.github.shiniseong.beyondtest.services.prescription.application.service.PrescriptionCodeGrpcService
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.toPrescriptionCodeStatus
import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.concurrent.TimeUnit

class PrescriptionCodeGrpcServiceAdapterTest : StringSpec({
    val repository = PrescriptionCodeMockRepository()
    val service = PrescriptionCodeGrpcService(repository)
    val grpcService = PrescriptionCodeGrpcServiceAdapter(service)
    val server = ServerBuilder.forPort(0).addService(grpcService).build()
    beforeSpec {
        server.start()
    }

    afterSpec {
        server.shutdown()
        server.awaitTermination(5, TimeUnit.SECONDS)
    }

    beforeTest {
        repository.clearAll()
    }
    "findAllPrescriptionCodeByUserIdAndStatus는 사용자 ID와 상태에 맞는 처방 코드를 반환해야 한다." {
        // given
        val userId = "userId123"
        val status = PrescriptionCodeStatus.ACTIVATED
        val code1 = "ABCD1234"
        val code2 = "EFGH5678"
        val code3 = "IJKL9012"
        val hospitalId = "hospitalId123"
        val prescriptionCode1 = PrescriptionCode.create(code = code1, hospitalId = hospitalId)
        val prescriptionCode2 = PrescriptionCode.create(code = code2, hospitalId = hospitalId)
        val prescriptionCode3 = PrescriptionCode.create(code = code3, hospitalId = hospitalId)
        val channel = ManagedChannelBuilder.forAddress("localhost", server.port)
            .usePlaintext()
            .build()

        val stub = PrescriptionCodeServiceGrpc.newBlockingStub(channel)

        repository.insert(prescriptionCode1)
        repository.insert(prescriptionCode2)
        repository.insert(prescriptionCode3)

        repository.update(prescriptionCode1.activateFor(userId))
        repository.update(prescriptionCode2.activateFor(userId))

        val request = FindPrescriptionCodeByUserIdAndStatusRequest.newBuilder().apply {
            this.userId = userId
            this.status = status.name
        }.build()
        // when
        val result = stub.findAllByUserIdAndStatus(request)
        // then
        result.prescriptionCodesList.size shouldBe 2
        result.prescriptionCodesList.map { it.code } shouldBe listOf(code1, code2)
        result.prescriptionCodesList.all { it.status.toPrescriptionCodeStatus().isActivated() } shouldBe true
        result.prescriptionCodesList.all { it.activatedFor == userId } shouldBe true
    }
})