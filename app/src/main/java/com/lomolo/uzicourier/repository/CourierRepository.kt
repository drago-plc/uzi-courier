package com.lomolo.uzicourier.repository

import com.google.android.gms.maps.model.LatLng
import com.lomolo.uzicourier.SetCourierStatusMutation
import com.lomolo.uzicourier.TrackCourierGpsMutation
import com.lomolo.uzicourier.network.UziGqlApiInterface

interface CourierInterface {
    suspend fun trackCourierGps(gps: LatLng): TrackCourierGpsMutation.Data
    suspend fun setCourierStatus(status: String): SetCourierStatusMutation.Data
}

class CourierRepository(
    private val uziGqlApiService: UziGqlApiInterface,
): CourierInterface {
    override suspend fun trackCourierGps(gps: LatLng): TrackCourierGpsMutation.Data {
        return uziGqlApiService.trackCourierGps(gps).dataOrThrow()
    }

    override suspend fun setCourierStatus(status: String): SetCourierStatusMutation.Data {
        return uziGqlApiService.setCourierStatus(status).dataOrThrow()
    }
}