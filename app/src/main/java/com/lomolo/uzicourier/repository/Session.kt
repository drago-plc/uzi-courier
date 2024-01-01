package com.lomolo.uzicourier.repository

import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.model.SignIn
import com.lomolo.uzicourier.network.UziRestApiServiceInterface
import com.lomolo.uzicourier.sql.dao.SessionDao
import kotlinx.coroutines.flow.Flow

interface SessionInterface {
    fun getSession(): Flow<List<Session>>
    suspend fun signIn(input: SignIn)
    suspend fun onboardUser(sessionId: Int, input: SignIn)
}

class SessionRepository(
    private val sessionDao: SessionDao,
    private val uziRestApiService: UziRestApiServiceInterface
): SessionInterface {
    override fun getSession() = sessionDao.getSession()
    override suspend fun signIn(input: SignIn) {
        val res = uziRestApiService.signIn(input)
        sessionDao.createSession(
           createLocalUserSession(res)
        )
    }

    override suspend fun onboardUser(sessionId: Int, input: SignIn) {
        val res = uziRestApiService.onboardUser(input)
        sessionDao.updateSession(
           updateLocalUserSession(sessionId, res)
        )
    }

    private fun createLocalUserSession(session: Session): Session {
        return Session(
            token = session.token,
            courierStatus = session.courierStatus,
            isCourier = session.isCourier,
            onboarding = session.onboarding
        )
    }
    private fun updateLocalUserSession(sessionId: Int, session: Session): Session {
        return Session(
            id = sessionId,
            token = session.token,
            courierStatus = session.courierStatus,
            isCourier = session.isCourier,
            onboarding = session.onboarding
        )
    }
}