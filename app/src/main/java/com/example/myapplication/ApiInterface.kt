package com.example.myapplication

import androidx.annotation.Keep
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("userAgent: retrofit-123")
    @POST("v1/GetProfile")
    fun getAB(@Body req: GetProfileRequest) : Call<GetProfileResponse>

    companion object {

        var BASE_URL = "https://ab.staging.okcredit.in/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(ApiInterface::class.java)

        }

    }
}

@Keep
data class GetProfileRequest(
    val merchant_id: String,
    val device_id: String
)

@Keep
data class GetProfileResponse(
    val profile: Profile
)

@Keep
data class Profile(
    val merchant_id: String,
    val features: Map<String, Boolean>,
    val experiments: Map<String, Experiment>
)

@Keep
data class Experiment(
    val name: String,
    val status: Int,
    val variant: String,
    val vars: Map<String, String>
)