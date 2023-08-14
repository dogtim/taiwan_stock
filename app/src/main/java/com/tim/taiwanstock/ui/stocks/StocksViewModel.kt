package com.tim.taiwanstock.ui.stocks

import LoggingInterceptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tim.lib.download.getCurrentDate
import com.tim.taiwanstock.network.StockApiService
import com.tim.taiwanstock.network.StockDataResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StocksViewModel : ViewModel() {
    private val retrofit: Retrofit
    private val stockApiService: StockApiService
    private val stockNumbers = listOf("2330", "2337", "1301", "1303", "1326", "6505")

    private val _itemList: MutableStateFlow<List<StockDataResponse>>  = MutableStateFlow(emptyList())
    val itemList: StateFlow<List<StockDataResponse>> = _itemList.asStateFlow()

    private val enableLog = false
    init {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://www.twse.com.tw/")
            .addConverterFactory(GsonConverterFactory.create())
        if (enableLog) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor())
                .build()
            retrofitBuilder.client(okHttpClient)
        }
        retrofit = retrofitBuilder.build()
        stockApiService = retrofit.create(StockApiService::class.java)
    }

    fun fetchItems() {
        // Fetch your items from wherever you need (e.g., API, database)
        viewModelScope.launch {
            // Update itemList with the fetched items
            try {
                val responses: MutableList<StockDataResponse> = mutableListOf()
                stockNumbers.forEach {number ->
                    responses.add(stockApiService.getStockData(getCurrentDate(), number))
                }
                _itemList.value = responses
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }


}

data class StockItem(
    val id: String,
    val closingPrice: String
)