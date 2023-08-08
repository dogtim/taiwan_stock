package com.tim.taiwanstock.ui.stocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tim.taiwanstock.network.StockApiService
import com.tim.taiwanstock.network.StockDataResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StocksViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.twse.com.tw/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val stockApiService = retrofit.create(StockApiService::class.java)
    private val stockNumbers = listOf("2330", "2337", "1301", "1303", "1326", "6505")

    private val _itemList: MutableStateFlow<List<StockDataResponse>>  = MutableStateFlow(emptyList())
    val itemList: StateFlow<List<StockDataResponse>> = _itemList.asStateFlow()

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

    private fun getCurrentDate(): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return LocalDate.now().format(dateFormatter)
    }
}