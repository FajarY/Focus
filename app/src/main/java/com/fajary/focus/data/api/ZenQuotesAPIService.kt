package com.fajary.focus.data.api

import com.fajary.focus.data.model.ZenQuote
import retrofit2.http.GET

interface ZenQuotesAPIService {
    @GET("api/today")
    suspend fun getTodayQuote(
    ): List<ZenQuote>

    @GET("api/random")
    suspend fun getRandomQuote(
    ): List<ZenQuote>
}