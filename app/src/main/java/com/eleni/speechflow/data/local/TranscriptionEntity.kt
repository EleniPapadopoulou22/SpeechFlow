package com.eleni.speechflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transcriptions")
data class TranscriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val timestamp: Long,
    val durationMs: Long = 50000,
    val language: String = "el-GR",
    val type: TranscriptionType = TranscriptionType.LISTEN,
)

enum class TranscriptionType { LISTEN, SPEAK }

object SupportedLanguages {
    const val GREEK = "el-GR"
    const val ENGLISH = "en-US"
}