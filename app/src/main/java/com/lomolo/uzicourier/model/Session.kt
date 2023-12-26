package com.lomolo.uzicourier.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = false) val id: String,
    val token: String
) {
    constructor(): this("", "")
}
