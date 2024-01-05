package com.lomolo.uzicourier.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.lomolo.uzicourier.GetCourierDocumentsQuery

interface UziGqlApiInterface {
    suspend fun getCourierDocuments(): ApolloResponse<GetCourierDocumentsQuery.Data>
}

class UziGqlApiRepository(
    private val apolloClient: ApolloClient
): UziGqlApiInterface {
    override suspend fun getCourierDocuments(): ApolloResponse<GetCourierDocumentsQuery.Data> {
        return apolloClient.query(GetCourierDocumentsQuery()).execute()
    }
}