package com.za.addresssuggestion.domain.usecase

import com.za.addresssuggestion.domain.model.AddressSuggestion

interface GetAddressSuggestionsUseCase {
    suspend operator fun invoke(query: String, limit: Int = 5): List<AddressSuggestion>
}