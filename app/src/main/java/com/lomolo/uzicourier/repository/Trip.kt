package com.lomolo.uzicourier.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.lomolo.uzicourier.GetCourierTripQuery
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.sql.dao.TripDao
import kotlinx.coroutines.flow.Flow

interface TripInterface{
    suspend fun getCourierTrip(): ApolloResponse<GetCourierTripQuery.Data>
    fun getTrip(): Flow<List<Trip>>
}

class TripRepository(
    private val uziGqlApiRepository: UziGqlApiInterface,
    private val tripDao: TripDao
): TripInterface {
    override suspend fun getCourierTrip() = uziGqlApiRepository.getCourierTrip()
    override fun getTrip() = tripDao.getTrip()
}