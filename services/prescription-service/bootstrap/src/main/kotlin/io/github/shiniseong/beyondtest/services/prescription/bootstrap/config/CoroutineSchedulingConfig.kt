package io.github.shiniseong.beyondtest.services.prescription.bootstrap.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineSchedulingConfig {

    @Bean
    fun schedulerScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}