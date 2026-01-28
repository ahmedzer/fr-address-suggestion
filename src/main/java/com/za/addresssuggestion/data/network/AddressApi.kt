package com.za.addresssuggestion.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal data class FeatureProperties(
    val label: String,
    val city: String,
    val postcode: String
)

internal data class Feature(
    val properties: FeatureProperties
)

internal data class AddressResponse(
    val features: List<Feature>
)

internal interface AddressApi {
    @GET("search/")
    suspend fun autocomplete(
        @Query("q") query: String,
        @Query("limit") limit: Int
    ): Response<AddressResponse>
}