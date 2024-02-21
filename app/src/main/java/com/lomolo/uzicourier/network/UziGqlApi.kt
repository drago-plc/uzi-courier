package com.lomolo.uzicourier.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.google.android.gms.maps.model.LatLng
import com.lomolo.uzicourier.CreateCourierDocumentMutation
import com.lomolo.uzicourier.GetCourierDocumentsQuery
import com.lomolo.uzicourier.GetCourierTripQuery
import com.lomolo.uzicourier.SetCourierStatusMutation
import com.lomolo.uzicourier.TrackCourierGpsMutation
import com.lomolo.uzicourier.type.UploadFile

interface UziGqlApiInterface {
    suspend fun getCourierDocuments(): ApolloResponse<GetCourierDocumentsQuery.Data>
    suspend fun createCourierDocument(type: UploadFile, uri: String): ApolloResponse<CreateCourierDocumentMutation.Data>
    suspend fun trackCourierGps(gps: LatLng): ApolloResponse<TrackCourierGpsMutation.Data>
    suspend fun setCourierStatus(status: String): ApolloResponse<SetCourierStatusMutation.Data>
    suspend fun getCourierTrip(): ApolloResponse<GetCourierTripQuery.Data>
}

class UziGqlApiRepository(
    private val apolloClient: ApolloClient
): UziGqlApiInterface {
    override suspend fun getCourierDocuments(): ApolloResponse<GetCourierDocumentsQuery.Data> {
        return apolloClient.query(GetCourierDocumentsQuery()).execute()
    }

    override suspend fun createCourierDocument(type: UploadFile, uri: String): ApolloResponse<CreateCourierDocumentMutation.Data> {
        return apolloClient.mutation(
            CreateCourierDocumentMutation(uri = uri, type = type)
        ).execute()
    }

    override suspend fun trackCourierGps(gps: LatLng): ApolloResponse<TrackCourierGpsMutation.Data> {
        return apolloClient.mutation(
            TrackCourierGpsMutation(gps.latitude, gps.longitude)
        ).execute()
    }

    override suspend fun setCourierStatus(status: String): ApolloResponse<SetCourierStatusMutation.Data> {
        return apolloClient.mutation(
            SetCourierStatusMutation(status)
        ).execute()
    }

    override suspend fun getCourierTrip(): ApolloResponse<GetCourierTripQuery.Data> {
        return apolloClient.query(
            GetCourierTripQuery()
        ).execute()
    }
}