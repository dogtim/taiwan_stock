package com.tim.lib.download.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiService {
    @GET("rwd/zh/afterTrading/BWIBBU")
    @Streaming
    suspend fun downloadCsv(
        @Query("date") date: String,
        @Query("stockNo") stockNo: String,
        @Query("response") response: String = "html"
    ): ResponseBody
}

interface StockDataApi {
    // Example: Request URL: https://www.twse.com.tw/rwd/zh/afterTrading/BWIBBU?date=20230811&stockNo=2330&response=json
    @GET("rwd/zh/afterTrading/BWIBBU")
    @Streaming
    suspend fun downloadCsv(
        @Query("date") date: String,
        @Query("stockNo") stockNo: String,
        @Query("response") response: String = "json"
    ): StockData
}
data class StockData(
    val stat: String,
    val date: String,
    val title: String,
    val fields: List<String>,
    val data: List<List<String>>,
    val total: Int
)