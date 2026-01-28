package com.za.addresssuggestion

import com.za.addresssuggestion.data.network.AddressApi
import com.za.addresssuggestion.data.network.AddressResponse
import com.za.addresssuggestion.data.network.Feature
import com.za.addresssuggestion.data.network.FeatureProperties
import com.za.addresssuggestion.data.repository.GetAddressSuggestionsUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class GetAddressSuggestionsUseCaseImplTest {

    private val api: AddressApi = mockk()
    private val useCase = GetAddressSuggestionsUseCaseImpl(api)

    @Test
    fun `returns suggestions when API response is successful`() = runBlocking {
        val feature = Feature(FeatureProperties("123 Rue Test", "Paris", "75001"))
        val response = AddressResponse(listOf(feature))

        coEvery { api.autocomplete("123 Rue Test", any()) } returns Response.success(response)

        val result = useCase.invoke("123 Rue Test")

        assertEquals(1, result.size)
        assertEquals("123 Rue Test", result[0].label)
        assertEquals("Paris", result[0].city)
        assertEquals("75001", result[0].postalCode)
    }

    @Test
    fun `returns empty list when API fails`() = runBlocking {
        val errorBody = ResponseBody.create("application/json".toMediaType(), "{}")
        coEvery { api.autocomplete(any(), any()) } returns Response.error(500, errorBody)

        val result = useCase.invoke("random query")

        assertEquals(0, result.size)
    }
}