package com.example.bitcoinpricetracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BitcoinApi {
    @GET("api/v3/coins/bitcoin/market_chart")
    fun getBitcoinPrices(
        @Query("vs_currency") vsCurrency: String,
        @Query("from") startTime: String,
        @Query("to") endTime: String,
        @Query("interval") interval: String
    ): Call<Map<String, List<List<Float>>>>
}


