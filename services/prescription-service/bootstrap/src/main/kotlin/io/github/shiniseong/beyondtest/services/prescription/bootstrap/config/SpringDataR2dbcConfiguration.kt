package io.github.shiniseong.beyondtest.services.prescription.bootstrap.config

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class SpringDataR2dbcConfiguration : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        val configuration = H2ConnectionConfiguration.builder().apply {
            inMemory("testdb")
            username("sa")
        }.build()
        return H2ConnectionFactory(configuration)
    }
}