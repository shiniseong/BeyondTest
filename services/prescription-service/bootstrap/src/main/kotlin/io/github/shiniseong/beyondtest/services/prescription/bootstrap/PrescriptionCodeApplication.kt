package io.github.shiniseong.beyondtest.services.prescription.bootstrap

import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ComponentScan(basePackages = ["io.github.shiniseong.beyondtest"])
@EnableScheduling
class PrescriptionCodeApplication

fun main(args: Array<String>) {
    runApplication<PrescriptionCodeApplication>(*args) {
        addInitializers(
            // === Application ===
            applicationBeans,
            // === Inbound ===
            controllerBeans,
            grpcBeans,
            // === Outbound ===
            repositoryBeans,
            // === Configuration ===
            configurationBeans
        )
    }
}