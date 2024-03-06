package com.lomolo.uzicourier.container

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.lomolo.uzicourier.apollo.interceptors.AuthInterceptor
import com.lomolo.uzicourier.network.UziGqlApiInterface
import com.lomolo.uzicourier.network.UziGqlApiRepository
import com.lomolo.uzicourier.network.UziRestApiServiceInterface
import com.lomolo.uzicourier.repository.CourierInterface
import com.lomolo.uzicourier.repository.CourierRepository
import com.lomolo.uzicourier.repository.SessionInterface
import com.lomolo.uzicourier.repository.SessionRepository
import com.lomolo.uzicourier.repository.TripInterface
import com.lomolo.uzicourier.repository.TripRepository
import com.lomolo.uzicourier.sql.UziStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val uziRestApiService: UziRestApiServiceInterface
    val sessionRepository: SessionInterface
    val apolloClient: ApolloClient
    val uziGqlApiRepository: UziGqlApiInterface
    val courierRepository: CourierInterface
    val tripRepository: TripInterface
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
        .baseUrl(baseApi)
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
        .okHttpClient(okhttpClient)
        .httpServerUrl("${baseApi}/v1/api")
        .webSocketServerUrl("${wss}/subscription")
        .webSocketReopenWhen {_, attempt ->
            delay(attempt * 1000)
            true
        }
        .addHttpInterceptor(
            AuthInterceptor(
                UziStore.getStore(context).sessionDao(),
                sessionRepository
            )
        )
        .build()

    override val uziGqlApiRepository: UziGqlApiInterface by lazy {
        UziGqlApiRepository(apolloClient)
    }

    override val courierRepository: CourierInterface by lazy {
        CourierRepository(uziGqlApiRepository)
    }

    override val tripRepository: TripInterface by lazy {
        TripRepository(
            uziGqlApiRepository,
            UziStore.getStore(context).tripDao()
        )
    }
}