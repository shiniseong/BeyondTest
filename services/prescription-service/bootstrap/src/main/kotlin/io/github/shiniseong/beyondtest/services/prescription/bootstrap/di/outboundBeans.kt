package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.repoimpl.PrescriptionCodeRepository
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
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
    bean<PrescriptionCodeRepositoryPort> {
        val template = ref<R2dbcEntityTemplate>()
        PrescriptionCodeRepository(template)
    }
}