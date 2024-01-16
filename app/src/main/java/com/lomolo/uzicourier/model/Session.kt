package com.lomolo.uzicourier.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey val id: String = "",
    val token: String = "",
    val phone: String = "",
    val courierStatus: CourierStatus = CourierStatus.ONBOARDING,
    val isCourier: Boolean = false,
    val onboarding: Boolean = true
)

enum class CourierStatus{OFFLINE, ONLINE, ONBOARDING}