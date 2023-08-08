package com.tim.taiwanstock.network

import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    // 日收盤價及月平均收盤價
    @GET("rwd/zh/afterTrading/STOCK_DAY_AVG")
    suspend fun getStockData(
        @Query("date") date: String,
        @Query("stockNo") stockNo: String,
        @Query("response") response: String = "json"
    ): StockDataResponse

}

data class StockDataResponse(
    val stat: String,
    val date: String,
    val title: String,
    val fields: List<String>,
    val data: List<List<String>>,
    val notes: List<String>,
    val hints: String
)
