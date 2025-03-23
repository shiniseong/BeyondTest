package io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.repoimpl

import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.entitiy.PrescriptionCodeEntity
import io.github.shiniseong.beyondtest.services.prescription.adapter.outbound.repository.entitiy.toEntity
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query

class PrescriptionCodeRepository(
    private val entityTemplate: R2dbcEntityTemplate
) : PrescriptionCodeRepositoryPort {
    private val entityClazz = PrescriptionCodeEntity::class.java

    override suspend fun insert(prescriptionCode: PrescriptionCode): PrescriptionCode =
        entityTemplate.insert(prescriptionCode.toEntity()).awaitSingle().toDomain()

    override suspend fun update(prescriptionCode: PrescriptionCode): PrescriptionCode =
        entityTemplate.update(prescriptionCode.toEntity()).awaitSingle().toDomain()

    override suspend fun findByCode(codeValue: String): PrescriptionCode? =
        loadByCode(codeValue)?.toDomain()

    override suspend fun findAllByUserIdAndStatus(
        userId: String,
        status: PrescriptionCodeStatus
    ): List<PrescriptionCode> {
        val query = query(where("activated_for").`is`(userId).and("status").`is`(status.name))
        return entityTemplate.select(query, entityClazz)
            .map { it.toDomain() }
            .asFlow()
            .toList()
    }

    private suspend fun loadByCode(code: String): PrescriptionCodeEntity? =
        entityTemplate
            .selectOne(query(where("code").`is`(code)), entityClazz)
            .awaitSingleOrNull()
}