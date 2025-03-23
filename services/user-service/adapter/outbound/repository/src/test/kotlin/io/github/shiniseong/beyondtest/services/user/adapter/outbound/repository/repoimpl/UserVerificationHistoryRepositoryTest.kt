package io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.repoimpl

import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory
import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.await

class UserVerificationHistoryRepositoryTest : StringSpec({
    // H2 In-Memory DB를 사용하기 위한 설정
    lateinit var connectionFactory: ConnectionFactory
    lateinit var entityTemplate: R2dbcEntityTemplate
    lateinit var repository: UserVerificationHistoryRepository

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
        repository = UserVerificationHistoryRepository(entityTemplate)

        // 테이블 초기 생성
        runBlocking {
            val createTableQuery = """
                CREATE TABLE IF NOT EXISTS user_verification_histories (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id VARCHAR(100) NOT NULL,
                    version VARCHAR(20) NOT NULL,
                    os VARCHAR(20) NOT NULL,
                    mode VARCHAR(20) NOT NULL,
                    hash VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    verified BOOLEAN NOT NULL,
                    message VARCHAR(500)
                )
            """.trimIndent()

            entityTemplate.databaseClient.sql(createTableQuery).await()
        }
    }

    beforeTest {
        // 각 테스트 사이에 테이블 내용 지우기
        runBlocking {
            entityTemplate.databaseClient.sql("DELETE FROM user_verification_histories")
                .fetch()
                .rowsUpdated()
                .awaitFirstOrNull()
        }
    }

    "insert는 검증 이력을 삽입하고 삽입된 도메인 객체를 반환해야 한다." {
        runBlocking {
            // given
            val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
            val version = AppVersion.parse("1.0.0")
            val os = OS.ANDROID
            val mode = BuildMode.DEBUG
            val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
            val createdAt = LocalDateTime.parse("2024-03-24T10:30:00")
            val verified = true
            val message = "검증 성공"

            val userVerificationHistory = UserVerificationHistory(
                id = 0,
                userId = userId,
                version = version,
                os = os,
                mode = mode,
                hash = hash,
                createdAt = createdAt,
                verified = verified,
                message = message
            )

            // when
            val inserted = repository.insert(userVerificationHistory)

            // then
            inserted.userId shouldBe userId
            inserted.version shouldBe version
            inserted.os shouldBe os
            inserted.mode shouldBe mode
            inserted.hash shouldBe hash
            inserted.createdAt shouldBe createdAt
            inserted.verified shouldBe verified
            inserted.message shouldBe message
            inserted.id shouldBe 1 // 자동 증가된 ID 확인
        }
    }

    "getAll은 모든 검증 이력을 반환해야 한다." {
        runBlocking {
            // given
            val userId1 = "e4e3ecbd-2208-4905-8120-426473d0eae9"
            val userId2 = "f5f4fdce-3319-5016-9231-537584e1fba0"
            val version1 = AppVersion.parse("1.0.0")
            val version2 = AppVersion.parse("1.1.0")
            val createdAt1 = LocalDateTime.parse("2024-03-24T10:30:00")
            val createdAt2 = LocalDateTime.parse("2024-03-24T11:45:00")

            val history1 = UserVerificationHistory(
                id = 0,
                userId = userId1,
                version = version1,
                os = OS.ANDROID,
                mode = BuildMode.DEBUG,
                hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU=",
                createdAt = createdAt1,
                verified = true,
                message = "검증 성공"
            )

            val history2 = UserVerificationHistory(
                id = 0,
                userId = userId2,
                version = version2,
                os = OS.IOS,
                mode = BuildMode.RELEASE,
                hash = "Z06VMujFG1vYOr8gTOb2FFzQ1GW=",
                createdAt = createdAt2,
                verified = false,
                message = "검증 실패: 유효하지 않은 해시"
            )

            // 이력 삽입
            repository.insert(history1)
            repository.insert(history2)

            // when
            val allHistories = repository.getAll()

            // then
            allHistories.size shouldBe 2

            // 각 이력의 userId 값들을 모은 리스트가 예상한 userId 값들을 모두 포함하는지 확인
            allHistories.map { it.userId } shouldContainAll listOf(userId1, userId2)

            // 첫 번째 이력 상세 검증
            val firstHistory = allHistories.first { it.userId == userId1 }
            firstHistory.version shouldBe version1
            firstHistory.os shouldBe OS.ANDROID
            firstHistory.mode shouldBe BuildMode.DEBUG
            firstHistory.hash shouldBe "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
            firstHistory.createdAt shouldBe createdAt1
            firstHistory.verified shouldBe true
            firstHistory.message shouldBe "검증 성공"

            // 두 번째 이력 상세 검증
            val secondHistory = allHistories.first { it.userId == userId2 }
            secondHistory.version shouldBe version2
            secondHistory.os shouldBe OS.IOS
            secondHistory.mode shouldBe BuildMode.RELEASE
            secondHistory.hash shouldBe "Z06VMujFG1vYOr8gTOb2FFzQ1GW="
            secondHistory.createdAt shouldBe createdAt2
            secondHistory.verified shouldBe false
            secondHistory.message shouldBe "검증 실패: 유효하지 않은 해시"
        }
    }

    "insert 후 getAll을 사용하면 삽입된 이력이 포함되어야 한다." {
        runBlocking {
            // given
            val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
            val version = AppVersion.parse("1.0.0")
            val os = OS.ANDROID
            val mode = BuildMode.DEBUG
            val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="
            val createdAt = LocalDateTime.parse("2024-03-24T10:30:00")

            val history = UserVerificationHistory(
                id = 0,
                userId = userId,
                version = version,
                os = os,
                mode = mode,
                hash = hash,
                createdAt = createdAt,
                verified = true,
                message = "검증 성공"
            )

            // when
            repository.insert(history)
            val allHistories = repository.getAll()

            // then
            allHistories.size shouldBe 1
            val retrievedHistory = allHistories.first()
            retrievedHistory.userId shouldBe userId
            retrievedHistory.version shouldBe version
            retrievedHistory.os shouldBe os
            retrievedHistory.mode shouldBe mode
            retrievedHistory.hash shouldBe hash
            retrievedHistory.createdAt shouldBe createdAt
            retrievedHistory.verified shouldBe true
            retrievedHistory.message shouldBe "검증 성공"
        }
    }

    "성공 및 실패 검증 이력 모두 정상적으로 저장되어야 한다." {
        runBlocking {
            // given
            val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
            val version = AppVersion.parse("1.0.0")
            val os = OS.ANDROID
            val mode = BuildMode.DEBUG
            val hash = "Y95ULTuEF0uXNq7fSNa1EEzP0FU="

            // 성공 이력
            val successHistory = UserVerificationHistory.success(
                hash = hash,
                userId = userId,
                version = version,
                os = os,
                mode = mode
            )

            // 실패 이력
            val failureHistory = UserVerificationHistory.failure(
                message = "App 검증에 실패했습니다. Hash값이 유효하지 않습니다.",
                userId = userId,
                version = version,
                os = os,
                mode = mode,
                hash = "InvalidHash"
            )

            // when
            repository.insert(successHistory)
            repository.insert(failureHistory)
            val allHistories = repository.getAll()

            // then
            allHistories.size shouldBe 2

            // 성공 이력 검증
            val retrievedSuccessHistory = allHistories.first { it.verified }
            retrievedSuccessHistory.userId shouldBe userId
            retrievedSuccessHistory.hash shouldBe hash
            retrievedSuccessHistory.verified shouldBe true

            // 실패 이력 검증
            val retrievedFailureHistory = allHistories.first { !it.verified }
            retrievedFailureHistory.userId shouldBe userId
            retrievedFailureHistory.hash shouldBe "InvalidHash"
            retrievedFailureHistory.verified shouldBe false
            retrievedFailureHistory.message shouldBe "App 검증에 실패했습니다. Hash값이 유효하지 않습니다."
        }
    }
})