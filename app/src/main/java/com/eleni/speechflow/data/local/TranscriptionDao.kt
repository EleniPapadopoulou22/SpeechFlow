package com.eleni.speechflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranscriptionDao {
    @Query("SELECT * FROM transcriptions ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<TranscriptionEntity>>

    @Query("SELECT * FROM transcriptions WHERE id = :id")
    suspend fun findById(id: Long): TranscriptionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TranscriptionEntity): Long

    @Delete
    suspend fun delete(entity: TranscriptionEntity)

    @Query("DELETE FROM transcriptions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM transcriptions")
    fun observeCount(): Flow<Int>
}