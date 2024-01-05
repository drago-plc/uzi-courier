package com.lomolo.uzicourier

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.network.UziGqlApiRepository
import com.lomolo.uzicourier.network.UziRestApiServiceInterface
import com.lomolo.uzicourier.repository.SessionInterface
import com.lomolo.uzicourier.repository.SessionRepository
import com.lomolo.uzicourier.sql.UziStore
import com.lomolo.uzicourier.sql.dao.SessionDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val uziRestApiService: UziRestApiServiceInterface
    val sessionRepository: SessionInterface
    val apolloClient: ApolloClient
    val uziGqlApiRepository: UziGqlApiInterface
}

class AuthInterceptor(
    private val sessionRepository: SessionDao
): HttpInterceptor {
    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        val token = mutex.withLock {
            sessionRepository
                .getSession()
                .firstOrNull()
        }

        return if (token!!.isNotEmpty()) {
            chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer ${token.first().token}").build()
            )
        } else {
            chain.proceed(
                request.newBuilder().build()
            )
        }
    }
}

class DefaultContainer(private val context: Context): AppContainer {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okhttpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .callTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://948d-102-217-127-1.ngrok-free.app")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okhttpClient)
        .build()

    override val uziRestApiService: UziRestApiServiceInterface by lazy {
        retrofit.create(UziRestApiServiceInterface::class.java)
    }

    override val sessionRepository: SessionInterface by lazy {
        SessionRepository(
            sessionDao = UziStore.getStore(context).sessionDao(),
            uziRestApiService = uziRestApiService
        )
    }

    override val apolloClient = ApolloClient.Builder()
        .serverUrl("https://948d-102-217-127-1.ngrok-free.app/api")
        .addHttpInterceptor(
            AuthInterceptor(
                UziStore.getStore(context).sessionDao()
            )
        )
        .build()

    override val uziGqlApiRepository: UziGqlApiInterface by lazy {
        UziGqlApiRepository(apolloClient)
    }
}