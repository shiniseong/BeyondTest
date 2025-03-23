package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.user.adapter.outbound.grpc.AppEnvironmentGrpcMockClient
import io.github.shiniseong.beyondtest.services.user.adapter.outbound.grpc.PrescriptionCodeGrpcClient
import io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.repoimpl.UserVerificationHistoryRepository
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.AppEnvironmentClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.prescriptioncode.PrescriptionCodeClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.repository.UserVerificationHistoryRepositoryPort
import org.springframework.context.support.beans
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.core.DatabaseClient

val repositoryBeans = beans {
    bean {
        val databaseClient = ref<DatabaseClient>()
        ConnectionFactoryInitializer().apply {
            setConnectionFactory(databaseClient.connectionFactory)
        }
    }
    bean<UserVerificationHistoryRepositoryPort> {
        val template = ref<R2dbcEntityTemplate>()
        UserVerificationHistoryRepository(template)
    }
}

val grpcBeans = beans {
    bean<PrescriptionCodeClientPort> {
        PrescriptionCodeGrpcClient(ref())
    }
    bean<AppEnvironmentClientPort> {
        AppEnvironmentGrpcMockClient()
    }
}