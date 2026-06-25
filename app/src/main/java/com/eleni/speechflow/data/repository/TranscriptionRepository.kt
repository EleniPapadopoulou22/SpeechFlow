package com.eleni.speechflow.data.repository


import com.eleni.speechflow.data.local.TranscriptionDao
import com.eleni.speechflow.data.local.TranscriptionEntity
import com.eleni.speechflow.data.local.TranscriptionType
import kotlinx.coroutines.flow.Flow

class TranscriptionRepository(private val dao: TranscriptionDao) {

    fun observeAll(): Flow<List<TranscriptionEntity>> = dao.observeAll()

    fun observeCount(): Flow<Int> = dao.observeCount()

    suspend fun save(
        text: String,
        durationMs: Long = 0,
        language: String = "el-GR",
        type: TranscriptionType = TranscriptionType.LISTEN,
    ): Long {
        val entity = TranscriptionEntity(
            text = text,
            timestamp = System.currentTimeMillis(),
            durationMs = durationMs,
            language = language,
            type = type,
        )
        return dao.insert(entity)
    }

    suspend fun delete(entity: TranscriptionEntity) = dao.delete(entity)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun findById(id: Long) = dao.findById(id)
}