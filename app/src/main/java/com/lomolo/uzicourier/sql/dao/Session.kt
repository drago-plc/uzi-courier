package com.lomolo.uzicourier.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lomolo.uzicourier.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createSession(session: Session)
    @Query("SELECT * FROM sessions LIMIT 1")
    fun getSession(): Flow<List<Session>>
    @Update
    fun updateSession(session: Session)
}
