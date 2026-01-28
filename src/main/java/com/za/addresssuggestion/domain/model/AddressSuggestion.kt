package com.za.addresssuggestion.domain.model

/**
 * Represents a suggested French address returned by the autocomplete API.
 *
 * @property label Full formatted address label
 * @property city City name
 * @property postalCode French postal code
 */
data class AddressSuggestion(
    val label: String,
    val city: String,
    val postalCode: String
)