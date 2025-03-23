package io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.mock

import io.github.shiniseong.beyondtest.services.user.application.port.outbound.repository.UserVerificationHistoryRepositoryPort
import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory

class UserVerificationHistoryMockRepository : UserVerificationHistoryRepositoryPort {
    private val userVerificationHistoryMap = mutableMapOf<Long, UserVerificationHistory>()
    override suspend fun insert(userVerificationHistory: UserVerificationHistory): UserVerificationHistory {
        val id = getNextId()
        val newUserVerificationHistory = userVerificationHistory.copy(id = id)
        userVerificationHistoryMap[id] = newUserVerificationHistory
        return newUserVerificationHistory
    }

    override suspend fun findAll(): List<UserVerificationHistory> {
        return userVerificationHistoryMap.values.toList()
    }

    private fun getNextId(): Long {
        return userVerificationHistoryMap.keys.maxOrNull()?.plus(1) ?: 1
    }
}