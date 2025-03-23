package io.github.shiniseong.beyondtest.services.user.application.port.outbound.repository

import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory

interface UserVerificationHistoryRepositoryPort {
    suspend fun insert(userVerificationHistory: UserVerificationHistory): UserVerificationHistory
    suspend fun findAll(): List<UserVerificationHistory>
}