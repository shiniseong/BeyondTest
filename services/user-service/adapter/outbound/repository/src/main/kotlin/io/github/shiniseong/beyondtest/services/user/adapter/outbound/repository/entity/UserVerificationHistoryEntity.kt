package io.github.shiniseong.beyondtest.services.user.adapter.outbound.repository.entity

import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory
import io.github.shiniseong.beyondtest.services.user.domain.enums.toBuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.toOs
import io.github.shiniseong.beyondtest.services.user.domain.vo.toAppVersion
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_verification_histories")
data class UserVerificationHistoryEntity @PersistenceCreator constructor(
    @Id @Column("id")
    val id: Long,
    @Column("user_id")
    val userId: String,
    @Column("version")
    val version: String,
    @Column("os")
    val os: String,
    @Column("mode")
    val mode: String,
    @Column("hash")
    val hash: String,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("verified")
    val verified: Boolean,
    @Column("message")
    val message: String?,
) {
    fun toDomain() = UserVerificationHistory(
        id = id,
        userId = userId,
        version = version.toAppVersion(),
        os = os.toOs(),
        mode = mode.toBuildMode(),
        hash = hash,
        createdAt = createdAt.toKotlinLocalDateTime(),
        verified = verified,
        message = message,
    )
}

fun UserVerificationHistory.toEntity() = UserVerificationHistoryEntity(
    id = id,
    userId = userId,
    version = version.toString(),
    os = os.toString(),
    mode = mode.toString(),
    hash = hash,
    createdAt = createdAt.toJavaLocalDateTime(),
    verified = verified,
    message = message,
)