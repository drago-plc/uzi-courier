package com.lomolo.uzicourier.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.google.android.gms.maps.model.LatLng
import com.lomolo.uzicourier.CreateCourierDocumentMutation
import com.lomolo.uzicourier.GetCourierDocumentsQuery
import com.lomolo.uzicourier.GetTripQuery
import com.lomolo.uzicourier.ReverseGeocodeQuery
import com.lomolo.uzicourier.SetCourierStatusMutation
import com.lomolo.uzicourier.TrackCourierGpsMutation
import com.lomolo.uzicourier.TripAssignmentSubscription
import com.lomolo.uzicourier.type.UploadFile
import kotlinx.coroutines.flow.Flow

interface UziGqlApiInterface {
    suspend fun getCourierDocuments(): ApolloResponse<GetCourierDocumentsQuery.Data>
    suspend fun createCourierDocument(type: UploadFile, uri: String): ApolloResponse<CreateCourierDocumentMutation.Data>
    suspend fun trackCourierGps(gps: LatLng): ApolloResponse<TrackCourierGpsMutation.Data>
    suspend fun setCourierStatus(status: String): ApolloResponse<SetCourierStatusMutation.Data>
    suspend fun getTrip(tripId: String): ApolloResponse<GetTripQuery.Data>
    fun tripAssignment(userId: String): Flow<ApolloResponse<TripAssignmentSubscription.Data>>
    suspend fun reverseGeocode(gps: LatLng): ApolloResponse<ReverseGeocodeQuery.Data>
}

class UziGqlApiRepository(
    private val apolloClient: ApolloClient
): UziGqlApiInterface {
    override suspend fun getCourierDocuments() = apolloClient.query(GetCourierDocumentsQuery()).execute()

    override suspend fun createCourierDocument(type: UploadFile, uri: String) = apolloClient.mutation(
            CreateCourierDocumentMutation(uri = uri, type = type)
        ).execute()

    override suspend fun trackCourierGps(gps: LatLng) = apolloClient.mutation(
            TrackCourierGpsMutation(gps.latitude, gps.longitude)
        ).execute()

    override suspend fun setCourierStatus(status: String) = apolloClient.mutation(
            SetCourierStatusMutation(status)
        ).execute()

    override suspend fun getTrip(tripId: String) = apolloClient.query(
            GetTripQuery(tripId)
        ).execute()

    override fun tripAssignment(userId: String) = apolloClient.subscription(TripAssignmentSubscription(userId)).toFlow()

    override suspend fun reverseGeocode(gps: LatLng) = apolloClient.query(
        ReverseGeocodeQuery(lat = gps.latitude, lng = gps.longitude)
    ).execute()
}