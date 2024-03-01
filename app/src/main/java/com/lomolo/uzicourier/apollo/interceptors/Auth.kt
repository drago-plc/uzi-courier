package com.lomolo.uzicourier.apollo.interceptors

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.lomolo.uzicourier.repository.SessionInterface
import com.lomolo.uzicourier.sql.dao.SessionDao
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthInterceptor(
    private val sessionDao: SessionDao,
    private val sessionRepository: SessionInterface
): HttpInterceptor {
    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        var session = mutex.withLock {
            sessionDao
                .getSession()
                .firstOrNull()
        }

        val response = chain.proceed(
            if (!session.isNullOrEmpty()) request.newBuilder().addHeader("Authorization", "Bearer ${session.first().token}").build() else request
        )

        return if (response.statusCode == 401) {
            session = mutex.withLock {
                sessionRepository.refreshSession(session!!.first())
                sessionDao
                    .getSession()
                    .firstOrNull()
            }

            chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer ${session!!.first().token}").build()
            )
        } else {
            response
        }
    }
}
