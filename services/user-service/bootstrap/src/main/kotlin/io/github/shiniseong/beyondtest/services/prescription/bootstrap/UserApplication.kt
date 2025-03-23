package io.github.shiniseong.beyondtest.services.prescription.bootstrap

import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.applicationBeans
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.configurationBeans
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.controllerBeans
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.di.repositoryBeans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.github.shiniseong.beyondtest"])
class UserApplication

fun main(args: Array<String>) {
    runApplication<UserApplication>(*args) {
        addInitializers(
            applicationBeans,
            configurationBeans,
            controllerBeans,
            repositoryBeans,

            )
    }
}