package com.lomolo.uzicourier.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.sql.dao.SessionDao
import com.lomolo.uzicourier.sql.dao.TripDao

@Database(entities = [Session::class, Trip::class], version=1, exportSchema=false)
abstract class UziStore: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var Instance: UziStore? = null

        fun getStore(context: Context): UziStore {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, UziStore::class.java, "uzistore.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}