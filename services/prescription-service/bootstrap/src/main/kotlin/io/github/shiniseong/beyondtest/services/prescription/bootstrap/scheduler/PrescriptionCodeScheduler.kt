package io.github.shiniseong.beyondtest.services.prescription.bootstrap.scheduler

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.system.PrescriptionCodeSystemUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled

class PrescriptionCodeScheduler(
    private val prescriptionCodeSystemService: PrescriptionCodeSystemUseCase,
    private val scope: CoroutineScope,
) {
    private val logger = LoggerFactory.getLogger(PrescriptionCodeScheduler::class.java)

    /**
     * 자정에 한번만 처리하는 것 보다. 1시간마다 처리하는 것이 더 안전하고 한번에 많은 업데이트가 일어나지 않는다.
     */
    @Scheduled(cron = "0 0 * * * ?")
    fun expirePrescriptionCodes() {
        logger.info("Starting prescription code expiration batch job")
        scope.launch {
            try {
                val expiredCodes = prescriptionCodeSystemService.expirePrescriptionCode()
                logger.info("Successfully expired ${expiredCodes.size} prescription codes")
                logger.debug(expiredCodes.toString())
            } catch (e: Exception) {
                logger.error("Error during prescription code expiration batch job", e)
            }
        }
    }
}