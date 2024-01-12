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
    suspend fun refreshSession(session: Session)
}

class SessionRepository(
    private val sessionDao: SessionDao,
    private val uziRestApiService: UziRestApiServiceInterface
): SessionInterface {
    override fun getSession() = sessionDao.getSession()
    override suspend fun signIn(input: SignIn) {
        val res = uziRestApiService.signIn(input)
        val newSession = Session(
            token = res.token,
            courierStatus = res.courierStatus,
            phone = res.phone,
            isCourier = res.isCourier,
            onboarding = res.onboarding
        )
        sessionDao.createSession(newSession)
    }

    override suspend fun refreshSession(session: Session) {
        val res = uziRestApiService.signIn(SignIn(phone = session.phone))
        val newSession = Session(
            id = session.id,
            token = res.token,
            courierStatus = res.courierStatus,
            phone = res.phone,
            isCourier = res.isCourier,
            onboarding = res.onboarding
        )
        sessionDao.updateSession(newSession)
    }

    override suspend fun onboardUser(sessionId: Int, input: SignIn) {
        val res = uziRestApiService.onboardUser(input)
        val newSession = Session(
            id = sessionId,
            token = res.token,
            phone = res.phone,
            courierStatus = res.courierStatus,
            isCourier = res.isCourier,
            onboarding = res.onboarding
        )
        sessionDao.updateSession(newSession)
    }
}