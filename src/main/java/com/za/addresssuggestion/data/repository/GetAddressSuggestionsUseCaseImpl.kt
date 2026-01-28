package com.za.addresssuggestion.data.repository

import com.za.addresssuggestion.data.network.AddressApi
import com.za.addresssuggestion.domain.model.AddressSuggestion
import com.za.addresssuggestion.domain.usecase.GetAddressSuggestionsUseCase

internal class GetAddressSuggestionsUseCaseImpl(
    private val api: AddressApi
) : GetAddressSuggestionsUseCase {

    override suspend fun invoke(
        query: String,
        limit: Int
    ): List<AddressSuggestion> {
        if (query.isBlank()) return emptyList()

        val response = api.autocomplete(query, limit)
        if (!response.isSuccessful) return emptyList()

        return response.body()?.features?.map {
            AddressSuggestion(
                label = it.properties.label,
                city = it.properties.city,
                postalCode = it.properties.postcode
            )
        }.orEmpty()
    }
}