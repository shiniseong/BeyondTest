package io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.entitiy

import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import io.github.shiniseong.beyondtest.services.prescription.domain.vo.toPrescriptionCodeValue
import io.github.shiniseong.beyondtest.shared.utils.now
import kotlinx.datetime.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("prescription_codes")
data class PrescriptionCodeEntity @PersistenceCreator constructor(
    @Id @Column("code")
    val code: String,
    @Column("status")
    val status: PrescriptionCodeStatus,
    @Column("created_by")
    val createdBy: String,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("activated_for")
    val activatedFor: String? = null,
    @Column("activated_at")
    val activatedAt: LocalDateTime? = null,
    @Column("expired_at")
    val expiredAt: LocalDateTime? = null,
) {
    fun toDomain() = PrescriptionCode(
        code = code.toPrescriptionCodeValue(),
        status = status,
        createdBy = createdBy,
        createdAt = createdAt,
        activatedFor = activatedFor,
        activatedAt = activatedAt,
        expiredAt = expiredAt,
    )
}

fun PrescriptionCode.toEntity() = PrescriptionCodeEntity(
    code = code.value,
    status = status,
    createdBy = createdBy,
    createdAt = createdAt,
    activatedFor = activatedFor,
    activatedAt = activatedAt,
    expiredAt = expiredAt,
)
