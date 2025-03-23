package io.github.shiniseong.beyondtest.services.prescription.bootstrap

import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.applicationBeans
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.controllerBeans
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.grpcBeans
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.repositoryBeans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.github.shiniseong.beyondtest"])
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
            repositoryBeans
        )
    }
}