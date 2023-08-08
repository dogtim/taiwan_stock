package com.tim.taiwanstock.ui.stocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tim.taiwanstock.network.StockApiService
import com.tim.taiwanstock.network.StockDataResponse
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
    private val _stockData = MutableLiveData<String>()
    val stockData: LiveData<String> = _stockData

    fun fetchStockData() {
        viewModelScope.launch {
            try {
                val response = stockApiService.getStockData(getCurrentFormattedDate(), "2330")
                val size = response.data.size
                _stockData.postValue(response.data[size - 2].last())
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    var itemList by mutableStateOf<StockDataResponse>(StockDataResponse("", "", "", listOf(), listOf(), listOf(), ""))
        private set
    fun fetchItems() {
        // Fetch your items from wherever you need (e.g., API, database)
        viewModelScope.launch {
            // Update itemList with the fetched items
            try {
                val response = stockApiService.getStockData(getCurrentFormattedDate(), "2330")
                itemList = response
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    fun getCurrentFormattedDate(): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return LocalDate.now().format(dateFormatter)
    }
}