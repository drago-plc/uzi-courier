package com.lomolo.uzicourier.network

import com.lomolo.uzicourier.model.Ipinfo
import com.lomolo.uzicourier.model.Session
import com.lomolo.uzicourier.model.SignIn
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UziRestApiServiceInterface {
    @GET("ipinfo")
    suspend fun getIpinfo(): Ipinfo
    @Headers("Content-Type: application/json")
    @POST("signin")
    suspend fun signIn(@Body input: SignIn): Session
    @Headers("Content-Type: application/json")
    @POST("user/onboard")
    suspend fun onboardUser(@Body input: SignIn): Session
    @Multipart
    @POST("courier/upload/document")
    suspend fun uploadImage(@Part body: MultipartBody.Part): UploadRes
}

data class UploadRes(
    val imageUri: String
)