package com.za.addresssuggestion.api

import com.za.addresssuggestion.data.network.AddressApi
import com.za.addresssuggestion.data.repository.GetAddressSuggestionsUseCaseImpl
import com.za.addresssuggestion.domain.model.AddressSuggestion
import com.za.addresssuggestion.domain.usecase.GetAddressSuggestionsUseCase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Entry point for French address autocompletion.
 *
 * This class uses the French government address API:
 * https://api-adresse.data.gouv.fr
 *
 * Features:
 * - 🇫🇷 France-only address suggestions
 * - No API key required
 * - No dependency on DI frameworks
 *
 * Typical usage:
 * ```
 * val autocomplete = AddressAutocomplete()
 * val results = autocomplete.getSuggestions("10 rue de")
 * ```
 */
class AddressAutocomplete {

    private val api: AddressApi
    private val useCase: GetAddressSuggestionsUseCase

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-adresse.data.gouv.fr/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        api = retrofit.create(AddressApi::class.java)
        useCase = GetAddressSuggestionsUseCaseImpl(api)
    }

    suspend fun getSuggestions(
        query: String,
        limit: Int = 5
    ): List<AddressSuggestion> {
        return useCase(query, limit)
    }
}