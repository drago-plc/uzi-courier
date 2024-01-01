package com.lomolo.uzicourier.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val token: String = "",
    val courierStatus: CourierStatus = CourierStatus.OFFLINE,
    val isCourier: Boolean = false,
    val onboarding: Boolean = true
)

enum class CourierStatus{OFFLINE, ONLINE}