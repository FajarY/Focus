package com.fajary.focus.data.repository

import com.fajary.focus.data.api.ZenQuotesAPIService
import com.fajary.focus.data.api.ZenQuotesRetrofitInstance
import com.fajary.focus.data.model.ZenQuote

class ZenQuotesRepository(
    private val api: ZenQuotesAPIService = ZenQuotesRetrofitInstance.api
) {
    suspend fun getTodayQuote(): ZenQuote =
        api.getTodayQuote()[0]

    suspend fun getRandomQuote(): ZenQuote =
        api.getRandomQuote()[0]
}