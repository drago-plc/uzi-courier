package com.lomolo.uzicourier.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.lomolo.uzicourier.CreateCourierDocumentMutation
import com.lomolo.uzicourier.GetCourierDocumentsQuery
import com.lomolo.uzicourier.type.UploadFile

interface UziGqlApiInterface {
    suspend fun getCourierDocuments(): ApolloResponse<GetCourierDocumentsQuery.Data>
    suspend fun createCourierDocument(type: UploadFile, uri: String): ApolloResponse<CreateCourierDocumentMutation.Data>
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
}