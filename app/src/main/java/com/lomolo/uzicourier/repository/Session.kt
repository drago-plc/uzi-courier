package com.lomolo.uzicourier.repository

import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.model.SignIn
import com.lomolo.uzicourier.network.UziRestApiServiceInterface
import com.lomolo.uzicourier.sql.dao.SessionDao
import kotlinx.coroutines.flow.Flow

interface SessionInterface {
    fun getSession(): Flow<List<Session>>
    suspend fun signIn(input: SignIn)
}

class SessionRepository(
    private val sessionDao: SessionDao,
    private val uziRestApiService: UziRestApiServiceInterface
): SessionInterface {
    override fun getSession() = sessionDao.getSession()
    override suspend fun signIn(input: SignIn) {
        val res = uziRestApiService.signIn(input)
        sessionDao.createSession(Session(id = res.id, token = res.token))
    }
}