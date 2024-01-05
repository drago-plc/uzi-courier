package com.lomolo.uzicourier.sql.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lomolo.uzicourier.model.Upload
import kotlinx.coroutines.flow.Flow

interface UploadsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createUpload(upload: Upload)
    @Query("SELECT * FROM uploads")
    fun getUploads(): Flow<List<Upload>>
    @Query("UPDATE uploads SET uploaded = :uploaded WHERE type = :type")
    suspend fun updateUpload(uploaded: Boolean, type: String)
}