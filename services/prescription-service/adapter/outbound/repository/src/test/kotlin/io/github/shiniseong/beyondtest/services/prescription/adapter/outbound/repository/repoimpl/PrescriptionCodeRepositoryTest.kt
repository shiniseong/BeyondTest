package io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.repoimpl

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.github.shiniseong.beyondtest.shared.utils.endOfDay
import io.github.shiniseong.beyondtest.shared.utils.plusWeeks
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.TransientDataAccessResourceException
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await

class PrescriptionCodeRepositoryTest : StringSpec({
    // H2 In-Memory DB를 사용하기 위한 설정
    lateinit var connectionFactory: ConnectionFactory
    lateinit var entityTemplate: R2dbcEntityTemplate
    lateinit var repository: PrescriptionCodeRepository

    beforeSpec {
        // 테스트 시작 전 테스트 앱이 종료 되기 전까지 H2 In-Memory DB를 유지하여 테스트
        connectionFactory = H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory("testdb")
                .username("sa")
                .password("")
                .option("DB_CLOSE_DELAY=-1")
                .build()
        )

        entityTemplate = R2dbcEntityTemplate(connectionFactory)
        repository = PrescriptionCodeRepository(entityTemplate)

        // 테이블 초기 생성
        runBlocking {
            val createTableQuery = """
                CREATE TABLE IF NOT EXISTS prescription_codes (
                    code VARCHAR(8) PRIMARY KEY,
                    status VARCHAR(20) NOT NULL,
                    created_by VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    activated_for VARCHAR(100),
                    activated_at TIMESTAMP,
                    expired_at TIMESTAMP
                )
            """.trimIndent()

            entityTemplate.databaseClient.sql(createTableQuery).await()
        }
    }

    beforeTest {
        // 각 테스트 사이에 테이블 내용 지우기
        runBlocking {
            entityTemplate.databaseClient.sql("DELETE FROM prescription_codes")
                .fetch()
                .rowsUpdated()
                .awaitFirstOrNull()
        }
    }
    "insert는 처방 코드를 삽입하고 삽입된 도메인 객체를 반환해야 한다." {
        runBlocking {
            // given
            val code = "ABCD1234"
            val hospitalId = "hospitalId123"
            val prescriptionCode = PrescriptionCode.create(code = code, hospitalId = hospitalId)


            val inserted = repository.insert(prescriptionCode)

            inserted shouldBe prescriptionCode
        }
    }

    "insert하려는 객체와 같은 PK의 객체가 이미 존재할 경우 DataIntegrityViolationException이 발생해야 한다." {
        runBlocking {
            // given
            val code = "ABCD1234"
            val hospitalId = "hospitalId123"
            val prescriptionCode = PrescriptionCode.create(code = code, hospitalId = hospitalId)

            // when & then
            val result = shouldThrow<DuplicateKeyException> {
                repository.insert(prescriptionCode)
                repository.insert(prescriptionCode)
            }

            ("Unique index or primary key violation" in result.message!!) shouldBe true
        }
    }

    "update는 처방 코드를 수정하고 수정된 도메인 객체를 반환해야 한다." {
        runBlocking {
            // given
            val code = "ABCD1234"
            val hospitalId = "hospitalId123"
            val userId = "userId123"
            val prescriptionCode = PrescriptionCode.create(code = code, hospitalId = hospitalId)
            repository.insert(prescriptionCode)

            // when
            val result = repository.update(prescriptionCode.activateFor("userId123"))

            // then
            result.status shouldBe PrescriptionCodeStatus.ACTIVATED
            result.activatedFor shouldBe userId
        }
    }

    "update는 업데이트 대상이 없을 때, 예외를 발생시켜야 한다." {
        runBlocking {
            // given
            val code = "ABCD1234"
            val hospitalId = "hospitalId123"
            val prescriptionCode = PrescriptionCode.create(code = code, hospitalId = hospitalId)

            // when & then
            val result = shouldThrow<TransientDataAccessResourceException> {
                repository.update(prescriptionCode)
            }

            ("does not exist" in result.message!!) shouldBe true
        }
    }

    "findByCode는 코드로 처방 코드를 찾아 반환해야 한다." {
        runBlocking {
            // given
            val code = "ABCD1234"
            val hospitalId = "hospitalId123"
            val prescriptionCode = PrescriptionCode.create(code = code, hospitalId = hospitalId)
            repository.insert(prescriptionCode)

            // when
            val result = repository.findByCode(code)

            // then
            result!!.code shouldBe prescriptionCode.code
        }
    }

    "findByCode는 코드로 찾을 수 없는 경우 null을 반환해야 한다." {
        runBlocking {
            // given
            val code = "ABCD1234"

            // when
            val result = repository.findByCode(code)

            // then
            result shouldBe null
        }
    }

    "findAllByUserIdAndStatus는 사용자 ID와 상태가 일치하는 처방 코드 목록을 반환해야 한다." {
        runBlocking {
            // given
            val hospitalId = "hospitalId123"
            val userId = "userId123"
            val prescriptionCode1 = PrescriptionCode.create(code = "ABCD1234", hospitalId = hospitalId)
            val prescriptionCode2 = PrescriptionCode.create(code = "EFGH5678", hospitalId = hospitalId)
            val prescriptionCode3 = PrescriptionCode.create(code = "IJKL9012", hospitalId = hospitalId)
            repository.insert(prescriptionCode1)
            repository.insert(prescriptionCode2)
            repository.insert(prescriptionCode3)
            repository.update(prescriptionCode1.activateFor(userId))
            repository.update(prescriptionCode2.activateFor(userId))

            // when
            val result = repository.findAllByUserIdAndStatus(userId, PrescriptionCodeStatus.ACTIVATED)

            // then
            result.size shouldBe 2
            result.map { it.code.value } shouldBe listOf("ABCD1234", "EFGH5678")
            result.all { it.status.isActivated() } shouldBe true
            result.all { it.activatedFor == userId } shouldBe true
        }
    }

    "findAllToExpireFrom함수는 from보다 만료일이 이전이거나 같은 처방 코드 목록을 반환해야 한다." {
        runBlocking {
            // given
            val hospitalId = "hospitalId123"
            val activatedAt = LocalDateTime(2025, 2, 10, 5, 0, 0)
            val fixedInstant = activatedAt.toInstant(TimeZone.currentSystemDefault())
            mockkObject(Clock.System)
            every { Clock.System.now() } returns fixedInstant
            val prescriptionCode1 = PrescriptionCode.create(code = "ABCD1234", hospitalId = hospitalId)
            val prescriptionCode2 = PrescriptionCode.create(code = "EFGH5678", hospitalId = hospitalId)
            val prescriptionCode3 = PrescriptionCode.create(code = "IJKL9012", hospitalId = hospitalId)
            repository.insert(prescriptionCode1)
            repository.insert(prescriptionCode2)
            repository.insert(prescriptionCode3)
            val activatedPrescriptionCode1 = prescriptionCode1.activateFor("userId123")
            val activatedPrescriptionCode2 = prescriptionCode2.activateFor("userId456")
            repository.update(activatedPrescriptionCode1)
            repository.update(activatedPrescriptionCode2)

            val expectedExpiredAt = activatedAt.plusWeeks(6).endOfDay()
            val codesToExpire = repository.findAllToExpireFrom(expectedExpiredAt)
            // when
            val result = repository.updateAll(codesToExpire.map { it.expired() })
            // then
            result.size shouldBe 2
            result.map { it.code.value } shouldBe listOf("ABCD1234", "EFGH5678")
            result.all { it.status.isExpired() } shouldBe true
        }
    }

})