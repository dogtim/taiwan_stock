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
object StockDataConstant {
    const val KEY_DATE = "Date"
    const val KEY_DATA = "DATA"
    const val KEY_DIVIDEND_YEAR = "Dividend Year"
    const val KEY_PRICE_TO_EARNINGS_RATIO = "Price-to-Earnings Ratio"
    const val KEY_PRICE_TO_BOOK_RATIO = "Price-to-Book Ratio"
    const val KEY_FINANCIAL_REPORT_YEAR_QUARTER = "Financial Report Year/Quarter"
}