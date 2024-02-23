package com.lomolo.uzicourier.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.lomolo.uzicourier.GetTripQuery
import com.lomolo.uzicourier.TripAssignmentSubscription
import com.lomolo.uzicourier.model.Trip
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.sql.dao.TripDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

interface TripInterface{
    suspend fun getCourierTrip(tripId: String): ApolloResponse<GetTripQuery.Data>
    fun getTrip(): Flow<List<Trip>>
    fun getTripAssignment(userId: String): Flow<ApolloResponse<TripAssignmentSubscription.Data>>
    suspend fun clearTrips()
}

class TripRepository(
    private val uziGqlApiRepository: UziGqlApiInterface,
    private val tripDao: TripDao
): TripInterface {
    override suspend fun getCourierTrip(tripId: String) = uziGqlApiRepository.getTrip(tripId)
    override fun getTrip() = tripDao.getTrip()
    override fun getTripAssignment(userId: String) = uziGqlApiRepository
        .tripAssignment(userId)
        .onEach {
            tripDao
                .createTrip(
                    Trip(
                        id = it.data?.assignTrip?.id.toString(),
                        status = it.data?.assignTrip?.status.toString(),
                    )
                )
        }

    override suspend fun clearTrips() = tripDao.clearTrips()
}