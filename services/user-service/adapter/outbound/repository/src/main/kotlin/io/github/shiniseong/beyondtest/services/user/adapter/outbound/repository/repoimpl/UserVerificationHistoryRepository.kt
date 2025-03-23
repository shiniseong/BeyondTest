package io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.repoimpl

import io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.entity.UserVerificationHistoryEntity
import io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.entity.toEntity
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.repository.UserVerificationHistoryRepositoryPort
import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

class UserVerificationHistoryRepository(
    private val entityTemplate: R2dbcEntityTemplate
) : UserVerificationHistoryRepositoryPort {
    private val entityClazz = UserVerificationHistoryEntity::class.java
    override suspend fun insert(userVerificationHistory: UserVerificationHistory): UserVerificationHistory =
        entityTemplate.insert(userVerificationHistory.toEntity()).awaitSingle().toDomain()


    override suspend fun getAll(): List<UserVerificationHistory> =
        entityTemplate
            .select(entityClazz).all()
            .map { it.toDomain() }
            .asFlow()
            .toList()
}